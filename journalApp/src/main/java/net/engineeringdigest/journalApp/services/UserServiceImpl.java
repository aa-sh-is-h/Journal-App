package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }

    @Override
    public void saveEntry(User user){ userRepository.save(user); }

    @Override
    public void deleteById(String id){
        userRepository.deleteById(id);
    }

    @Override
    public User findByUserName(String username){
        return userRepository.findByUserName(username);
    }

}
