package kr.easw.lesson07.service;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import kr.easw.lesson07.model.dto.UserAuthenticationDto;
import kr.easw.lesson07.model.dto.UserDataEntity;
import kr.easw.lesson07.model.repository.UserDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDataService {
    private final UserDataRepository repository;

    private final BCryptPasswordEncoder encoder;

    private final JwtService jwtService;

    @PostConstruct
    public void init() {
        System.out.println("Creating admin user");
        createUser(new UserDataEntity(0L, "admin", encoder.encode("admin"), true));
        createUser(new UserDataEntity(0L, "guest", encoder.encode("guest"), false));
    }

    public boolean isUserExists(String userId) {
        return repository.findUserDataEntityByUserId(userId).isPresent();
    }
    public void createUser(UserDataEntity entity) {
        repository.save(entity);
    }

    public List<UserDataEntity> getAllUsers() {
        return repository.findAll();
    }

    public boolean removeUser(String userId) {
        Optional<UserDataEntity> userOpt = repository.findByUserId(userId);
        if (userOpt.isPresent()) {
            repository.delete(userOpt.get());
            return true;
        }
        return false;
    }

    @Nullable
    public UserAuthenticationDto createTokenWith(UserDataEntity userDataEntity) {
        Optional<UserDataEntity> entity = repository.findUserDataEntityByUserId(userDataEntity.getUserId());
        if (entity.isEmpty()) throw new BadCredentialsException("Credentials invalid");
        UserDataEntity archivedEntity = entity.get();
        if (encoder.matches(userDataEntity.getPassword(), archivedEntity.getPassword()))
            return new UserAuthenticationDto(jwtService.generateToken(archivedEntity.getUserId()));
        throw new BadCredentialsException("Credentials invalid");
    }
}
