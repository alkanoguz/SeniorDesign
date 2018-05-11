<?php
include 'db_Config2.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

  $json = json_decode(file_get_contents('php://input'), true);
  ## $json2 = $json['register'];
    // Use a var_dump to learn what key holds the json data


    // let's say its in $_POST['json']


}

$response = array();
$response2 = array();

if (isset($json)) {
#      $name = $json['json']['artists'] ##lazım olcak
  $tck = $json['login']['tc_kimlik'];
  $psw = $json['login']['password'];
  $hashPass=md5(sha1($psw));

  $query = $db->prepare("SELECT uid,tckimlik,name,lname FROM uinf WHERE tckimlik = $tck");
  $query->execute();
  $get = $query->fetch();


  if ($query->rowcount() == 0)
{

  $response["hata"] = "Lütfen Tekrar Deneyin";
  $response2["message"] = $response;
  echo json_encode($response);
}

  else {
    $query2 = $db->prepare("SELECT psw,tckimlik FROM uinf WHERE tckimlik=$tck");
    $query2->execute();
    $get1 = $query2->fetch();
    if(($tck == $get1["tckimlik"]) && $hashPass == $get1["psw"]) {
      $response["uid"] = $get["uid"];
      $response["name"] = $get["name"];
      $response["surname"] = $get["lname"];
      $response2["message"] = $response;
      echo json_encode($response2);
    }

  }
}


else {
// required field is missing
$response["success"] = 0;
$response["message"] = "Required field(s) is missing";

// echoing JSON response
echo json_encode($response);
}
