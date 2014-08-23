<?php
$mysqli = new mysqli("localhost", "webservice", "generic", "logo_data");

if ($mysqli->connect_errno) {
    printf("{\"error\":\"Could not connect to MySQL DB!\"}");
    exit();
}

if(isset($_GET["id"]))
{
    $id = intval($_GET["id"]);
    
    if ($result = $mysqli->query("SELECT * FROM battles WHERE company_id = ".$id)) {
        
        while($row = mysqli_fetch_array($result)) {
            echo json_encode($row);
        }
        
        $result->close();
    }
}
else
{
    echo "{\"error\":\"Missing url params!\"}";
}


$mysqli->close();
?>