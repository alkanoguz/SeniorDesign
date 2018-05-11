
<?php
include 'db_Config3.php';

// Create connection
$conn = new mysqli($HostName, $HostUser, $HostPass, $DatabaseName);
$conn->set_charset("utf8");

 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
 $DefaultId = 0;

 $ImageData = $_POST['image_path'];

 $ImageName = $_POST['image_name'];
 $mesaj = $_POST['Mesaj'];

 $Adress = $_POST['adres'];

 $lng = $_POST['Longtitude'];

 $Lat = $_POST['Latitude'];
 $uid = $_POST['uid'];
 $uid = (int)$uid;


 $GetOldIdSQL ="SELECT id FROM imagetoservertable ORDER BY id ASC";

 $Query = mysqli_query($conn,$GetOldIdSQL);

 while($row = mysqli_fetch_array($Query)){

 $DefaultId = $row['id'];
 }

 $ImagePath = "images/$DefaultId.jpg";

 $ServerURL = "http://10.0.2.2/PHP/$ImagePath";

 $InsertSQL = "insert into imagetoservertable (image_path,image_name,Adres,Latitude,Longtitude,Message,uid) values ('$ServerURL','$ImageName','$Adress','$Lat','$lng','$mesaj','$uid')";

 if(mysqli_query($conn, $InsertSQL)){

 file_put_contents($ImagePath,base64_decode($ImageData));

 echo "Your Image Has Been Uploaded.";
 }

 mysqli_close($conn);
 }else{
 echo "Not Uploaded";
 }

?>
