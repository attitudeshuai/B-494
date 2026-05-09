package com.school.lostandfound.service;

import com.school.lostandfound.entity.Admin;

public interface AdminService {
    Admin login(String account, String password);
}
