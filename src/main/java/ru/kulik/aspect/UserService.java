package ru.kulik.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.kulik.dto.UserDTO;
import ru.kulik.exceptions.APIException;
import ru.kulik.repository.AccountRepository;
import ru.kulik.repository.UserRepository;

import java.util.UUID;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Before("@annotation(userValidation)")
    public void UserValidation(JoinPoint joinPoint, UserValidation userValidation) {
        Object[] args = joinPoint.getArgs();

        int uuidArgIndex = userValidation.uuidArgIndex();
        UUID uuid = (UUID) args[uuidArgIndex];

        userRepository.findByUuid(uuid).ifPresentOrElse(
                user -> {
                    log.info("Запрос на получение пользователя обработан успешно.");
                },
                () -> {
                    log.error("Ошибка получения пользователя с uuid {}", uuid);
                    throw new APIException("Пользователь с uuid: " + uuid + " не найден.");
                }
        );
    }

    @Before("@annotation(accountValidation)")
    public void accountValidation(JoinPoint joinPoint, AccountValidation accountValidation) {
        Object[] args = joinPoint.getArgs();
        int uuidArgsIndex = accountValidation.uuidArgIndex();
        UUID uuid = (UUID) args[uuidArgsIndex];

        accountRepository.findByUuid(uuid).ifPresentOrElse(
                account -> {
                    log.info("Запрос на получение счета обработан успешно.");
                },
                () -> {
                    log.error("Ошибка получения счета с uuid {}", uuid);
                    throw new APIException("Счет с uuid: " + uuid + " не найден.");
                }
        );
    }

    @Before("@annotation(UserNotNull)")
    public void userNotNull(JoinPoint joinPoint) {
        UserDTO user = (UserDTO) joinPoint.getArgs()[0];
        if (user != null) {
            log.info("Обработка метода сохранения пользователя.");
        } else {
            log.error("Данные не ссответсвуют ожидаемым.");
            throw new APIException("Получен пользователь без параметров.");
        }
    }

    @Before("execution(* ru.kulik.servise.UserServiceImp.findAll()))")
    public void findAll() {
        if (userRepository.findAll().isEmpty()) {
            log.warn("Список пользователей пуст.");
            throw new APIException("Список пользователей пуст.");
        } else {
            log.info("Получение списка всех пользователей. В БД содержится {} человек(а)", userRepository.findAll().size());
        }
    }
}
