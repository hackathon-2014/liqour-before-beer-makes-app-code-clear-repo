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
    if ($result = $mysqli->query("SELECT * FROM companies WHERE id = ".$id)) {

        while($row = mysqli_fetch_array($result)) {
            $company = array('id' => $row['id'], 'company_name' => $row['company_name'], "industry" => $row['industry']);
            echo json_encode($company);
        }

        $result->close();
    }
}
else
{
    if ($result = $mysqli->query("SELECT * FROM companies ORDER BY id")) {
        
        $companies = array();

        while($row = mysqli_fetch_array($result)) {
            $company = array('id' => $row['id'], 'company_name' => $row['company_name'], "industry" => $row['industry']);
            $companies[] = $company;
        }

        $wrapper = array("companies" => $companies);
        echo json_encode($wrapper);
        $result->close();
    }
}


$mysqli->close();
?>