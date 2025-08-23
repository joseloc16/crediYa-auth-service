package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository taskRepository;

    public Mono<User> saveTask(User task) {
        return taskRepository.save(task);
    }

    public Mono<User> updateTask(User task) {
        return taskRepository.save(task);
    }

    public Flux<User> getAllTasks() {
        return taskRepository.findAll();
    }

    public Mono<User> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    public Mono<Void> deleteTask(String id) {
        return taskRepository.deleteById(id);
    }
}
