<?php
error_reporting(0);
$dsn = 'mysql:host=localhost;dbname=deneme';
$user = 'root';
$password = 'slaechea1';
try {
    $db = new PDO($dsn, $user, $password);
    $db->exec('SET NAMES UTF8');
} catch (PDOException $e) {
    echo 'Connection failed: ' . $e->getMessage();
    exit();
}
