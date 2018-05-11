<?php
include 'db_config.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

  $json = json_decode(file_get_contents('php://input'), true);
  $json2 = $json['deneme'];
    // Use a var_dump to learn what key holds the json data


    // let's say its in $_POST['json']


}
$response = array();


    if (isset($json)) {
#      $name = $json['json']['artists'] ##lazım olcak
    $name = $json['deneme']['name'];
    $surname = $json['deneme']['surname'];
    $url = $json['deneme']['url'];




      $query = $db->prepare("INSERT INTO products(name,surname,url) VALUES('".$name."', '".$surname."','".$url."')");
      if($query->execute())




        if ($query) {

              $response["success"] = 1;
              $response["message"] = $name;
            echo json_encode($response);
          }
     else {
          // failed to insert row
          $response["success"] = 0;
          $response["message"] = "Oops! An error occurred.";

          // echoing JSON response
          echo json_encode($response);
}}
else{
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
;
#Comment
?>
