package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
//Business Login for Users

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);
//    there are many PasswordEncoders but we use BCryptPasswordEncoder here.

    @Override
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }

    @Override
    public void saveNewUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }

    public void saveExistingUser(User user){
        userRepository.save(user);
    }

    @Override
    public void deleteById(String id){
        userRepository.deleteById(id);
    }

    @Override
    public User findByUserName(String username){
        return userRepository.findByUserName(username);
    }

    @Override
    public void deleteByUserName(String username){
        userRepository.deleteByUserName(username);
    }

}
