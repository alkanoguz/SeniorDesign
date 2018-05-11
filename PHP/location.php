<?php
include 'db_Config3.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

  $json = json_decode(file_get_contents('php://input'), true);
  $json2 = $json['location'];
    // Use a var_dump to learn what key holds the json data


    // let's say its in $_POST['json']


}
$response = array();


    if (isset($json)) {
#      $name = $json['json']['artists'] ##lazım olcak
    $lat = $json['location']['lat'];
    $long = $json['location']['long'];




        $query = $db->prepare("INSERT INTO location(coordinate) VALUES((Point($lat,$long)))");
      if($query->execute())




        if ($query) {

              $response["success"] = 1;
              $response["message"] = $lat;
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
