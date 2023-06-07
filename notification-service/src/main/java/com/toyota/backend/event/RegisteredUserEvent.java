package com.toyota.backend.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author Arman Yaycı
 * @since 07.06.2023
 * Represents a registered user event.
 * This class is used to store information about a registered user in order to send mail.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredUserEvent {
    public String name;
    public String username;
    public String password;
    public String email;
}
