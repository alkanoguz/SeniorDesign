<?php
header('Content-Type: text/html; charset=utf-8');
include 'db_Config3.php';

// Create connection
try {
     $pdo = new PDO("mysql:host=localhost;dbname=deneme;charset=utf8", "root", "slaechea1");
} catch ( PDOException $e ){
     print $e->getMessage();
}
$statement = $pdo->prepare("SELECT image_name,image_path,message,Adres,Longtitude,Latitude FROM imagetoservertable ORDER BY id DESC");
$statement->execute();
$results = $statement->fetchAll(PDO::FETCH_ASSOC);
$response = json_encode(($results), JSON_UNESCAPED_UNICODE);
$response = str_replace("\/", "/", $response);
echo $response;

?>
