<?php
include 'db_Config2.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

  $json = json_decode(file_get_contents('php://input'), true);
  ## $json2 = $json['register'];
    // Use a var_dump to learn what key holds the json data


    // let's say its in $_POST['json']


}
$response = array();



  if (isset($json)) {
#      $name = $json['json']['artists'] ##lazım olcak
    $name = $json['register']['name'];
    $lname = $json['register']['lastname'];
    $mail = $json['register']['email'];
    $psw = $json['register']['password'];
    $hashPass=md5(sha1($psw));
    $tck = $json['register']['tc_kimlik'];
    $mphone = $json['register']['mobile_phone'];
    $address = $json['register']['address'];


    $query3 = $db->prepare("SELECT * FROM uinf WHERE tckimlik = $tck");
    $query3->execute();
    if ($query3->rowcount() >= 1)
    {
      $response["success"] = 0;
      $response["message"] = "Bu T.C Kayıtlı";
      echo json_encode($response);
    }
    else  {
        if ((strlen($psw) >= 8) && (filter_var($mail, FILTER_VALIDATE_EMAIL) == TRUE)) {
          $query = $db->prepare("INSERT INTO uinf(name,lname,psw,mail,mphone,adress,tckimlik)
          VALUES('".$name."','".$lname."','".$hashPass."','".$mail."','".$mphone."','".$address."','".$tck."')");

        ($query->execute());



        if ($query) {

              $response["success"] = 1;
              $response["message"] = $tck;
            echo json_encode($response);
          }
         else {
          // failed to insert row
          $response["success"] = 0;
          $response["message"] = "Oops! An error occurred.";

          // echoing JSON response
          echo json_encode($response);
        }}

    else if(strlen($psw) != 8 && filter_var($mail, FILTER_VALIDATE_EMAIL) == TRUE) {
      $response["success"] = 0;
      $response["message"] = "Şifre 8 karakter olmalı";
      echo json_encode($response);
    }
    else if(filter_var($mail, FILTER_VALIDATE_EMAIL) == FALSE && strlen($psw) == 8){
      $response["success"] = 0;
      $response["message"] = "Hatali Mail";
      echo json_encode($response);
    }
    else {
      $response["success"] = 0;
      $response["message"] = "Hatali Mail.Şifre 8 karakter olmalı.";
      echo json_encode($response);
    }

  }

}

    else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
};
?>
