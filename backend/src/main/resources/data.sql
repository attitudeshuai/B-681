-- 初始化示例数据 (Seed Data)
-- 注意: 使用 INSERT IGNORE 避免重复插入

INSERT IGNORE INTO students (id, name, student_id, gender, class_name, create_time, update_time) VALUES
(1, '张三', '2021001', '男', '计算机科学1班', NOW(), NOW()),
(2, '李四', '2021002', '女', '计算机科学1班', NOW(), NOW()),
(3, '王五', '2021003', '男', '计算机科学2班', NOW(), NOW()),
(4, '赵六', '2021004', '女', '软件工程1班', NOW(), NOW()),
(5, '孙七', '2021005', '男', '软件工程1班', NOW(), NOW()),
(6, '周八', '2021006', '女', '人工智能1班', NOW(), NOW()),
(7, '吴九', '2021007', '男', '人工智能1班', NOW(), NOW()),
(8, '郑十', '2021008', '女', '数据科学1班', NOW(), NOW());
