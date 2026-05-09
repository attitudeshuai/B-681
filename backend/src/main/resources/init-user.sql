-- 初始化用户数据
INSERT IGNORE INTO users (id, username, password, email, role, create_time) VALUES
(1, 'admin', '$2a$10$xQPJPEr3VZb7P.OJ/SZeXOY72qYY.kqJ3lQFxIzKHwzK.xqG4uLWi', 'admin@example.com', 'ADMIN', NOW()),
(2, 'user', '$2a$10$xQPJPEr3VZb7P.OJ/SZeXOY72qYY.kqJ3lQFxIzKHwzK.xqG4uLWi', 'user@example.com', 'USER', NOW());

-- 密码都是: admin123
