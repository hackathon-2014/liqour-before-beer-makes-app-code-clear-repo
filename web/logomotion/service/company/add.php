<?php
$mysqli = new mysqli("localhost", "webservice", "generic", "logo_data");

if ($mysqli->connect_errno) {
    printf("{\"error\":\"Could not connect to MySQL DB!\"}");
    exit();
}

if(isset($_GET["name"]))
{
    
    $cleanName = $mysqli->real_escape_string($_GET["name"]);
    if($mysqli->query("INSERT INTO companies ( `id`, `company_name`, `industry`) VALUES (NULL ,  '".$cleanName."', NULL)"))
    {      
        $company;
        if ($result = $mysqli->query("SELECT * FROM companies WHERE company_name = '".$cleanName."'")) {

            while($row = mysqli_fetch_array($result)) {
                $company = array('id' => $row['id'], 'company_name' => $row['company_name'], "industry" => $row['industry']);
            }

            $result->close();
        }
        
        if(isset($company) && $mysqli->query("INSERT INTO battles ( `company_id` ) VALUES ('".$company['id']."')"))
        {
            echo "{\"success\":\"Created company ".$cleanName."!\"}";
        }
        else
        {
             echo "{\"error\":\"Company not battle ready!\"}";
        }
        
    }
    else
    {
        echo "{\"error\":\"Failed to add company to table!  Duplicate name?\"}";
    }

}
else
{
    echo "{\"error\":\"name param not found!\"}";
}


$mysqli->close();
?>