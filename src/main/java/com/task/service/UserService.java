package com.task.service;

import com.task.constants.Constants;
import com.task.entity.UserRequestCounter;
import com.task.model.User;
import com.task.model.UserDTO;
import com.task.repository.UserRequestCounterRepository;
import jakarta.persistence.LockModeType;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRequestCounterRepository userRequestCounterRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ModelMapper modelMapper = setUpModelMapperMappings();

    public UserService(UserRequestCounterRepository userRequestCounterRepository) {
        this.userRequestCounterRepository = userRequestCounterRepository;
    }

    public UserDTO getUser(String login) {
        updateUserLoginCounter(login);
        try {
            User user = restTemplate.getForObject(Constants.GITHUB_API_URL + login, User.class);
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            userDTO.setCalculations(doCalculations(BigDecimal.valueOf(user.getFollowers()), BigDecimal.valueOf(user.getPublic_repos() + Constants.TWO)));
            log.info("Received data for user: " + login);
            return userDTO;
        }
        catch(HttpClientErrorException e) {
            log.warn("Failed to receive data for user: " + login);
            return null;
        }
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    private void updateUserLoginCounter(String login) {
        log.info("Login counter updated for user: " + login);
        Optional<UserRequestCounter> optionalUserRequestCounter = userRequestCounterRepository.findByLogin(login);
        UserRequestCounter userRequestCounter = optionalUserRequestCounter.orElse(new UserRequestCounter(login));
        userRequestCounter.setRequestCount(userRequestCounter.getRequestCount() + Constants.ONE);
        userRequestCounterRepository.save(userRequestCounter);
    }

    private BigDecimal doCalculations(BigDecimal followers, BigDecimal publicRepos) {
        return followers.equals(BigDecimal.ZERO)
                ? followers
                : BigDecimal.valueOf(Constants.SIX).divide(followers.multiply(publicRepos), Constants.TWO, RoundingMode.CEILING);
    }

    private ModelMapper setUpModelMapperMappings() {
        log.info("Setting up additional mappings for ModelMapper");
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<User, UserDTO> typeMap = modelMapper.createTypeMap(User.class, UserDTO.class);
        typeMap.addMappings(mapper -> {
            mapper.map(User::getAvatar_url, UserDTO::setAvatarUrl);
            mapper.map(User::getCreated_at, UserDTO::setCreatedAt);
        });
        return modelMapper;
    }
}
