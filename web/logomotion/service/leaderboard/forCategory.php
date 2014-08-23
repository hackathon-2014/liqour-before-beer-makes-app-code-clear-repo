<?php
$mysqli = new mysqli("localhost", "webservice", "generic", "logo_data");

if ($mysqli->connect_errno) {
    printf("{\"error\":\"Could not connect to MySQL DB!\"}");
    exit();
}

if(isset($_GET["category"]))
{
    $cleanName = $mysqli->real_escape_string($_GET["category"]);
    
    $order = 'DESC';
    if(isset($_GET["least"])){$order = 'ASC';}
    
    if ($result = $mysqli->query("SELECT company_id, ".$cleanName."_wins, ".$cleanName."_losses FROM battles ORDER BY ".$cleanName."_wins / (".$cleanName."_wins + ".$cleanName."_losses) ".$order." LIMIT 0, 10")) {
        $leaders = array();
        while($row = mysqli_fetch_array($result)) {
            $cId = $row['company_id'];
            $cWins = $row[$cleanName."_wins"];
            $cLosses = $row[$cleanName."_losses"];
            if($cLosses > 0){$cPercent = $cWins/($cWins+$cLosses);}
            else if($cWins > 0){$cPercent = 1;}
            else{$cPercent = 0;}
            
            $company = array('company_id' => $cId, 'percent' => $cPercent, 'wins' => $cWins, 'losses' => $cLosses);
            $leaders[] = $company;
        }
        
        echo json_encode($leaders);
        $result->close();
    }
}
else
{
    echo "{\"error\":\"Missing url params!\"}";
}


$mysqli->close();
?>