//package pl.coderslab.services;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import pl.coderslab.Role;
//import pl.coderslab.User;
//import pl.coderslab.repository.RoleRepository;
//import pl.coderslab.repository.UserRepository;
//import pl.coderslab.services.UserService;
//
//import java.util.Arrays;
//import java.util.HashSet;
//
//@Service
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
//                           BCryptPasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//    }
//
//    @Override
//    public User findByUserName(String username) {
//        return userRepository.findByNick(username);
//    }
//    @Override
//    public void saveUser(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setEnabled(1);
//        Role userRole = roleRepository.findByName("ROLE_USER");
//        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
//        userRepository.save(user);
//    }
//}
