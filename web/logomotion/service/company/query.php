<?php
$mysqli = new mysqli("localhost", "webservice", "generic", "logo_data");

/* check connection */
if ($mysqli->connect_errno) {
    printf("{\"error\":true}");
    exit();
}

if(isset($_GET["id"]))
{
    $id = intval($_GET["id"]);
    echo $id;
    if ($result = $mysqli->query("SELECT * FROM logos WHERE id = ".$id)) {

        while($row = mysqli_fetch_array($result)) {
          echo json_encode($row);
        }

        $result->close();
    }
}
else
{
    if ($result = $mysqli->query("SELECT * FROM logos ORDER BY id")) {
        
        $companies = array();

        while($row = mysqli_fetch_array($result)) {
            $company = array('company_name' => $row['company_name']);
            $companies[] = $company;
        }

        $wrapper = array("companies" => $companies);
        echo json_encode($wrapper);
        $result->close();
    }
}


$mysqli->close();
?>