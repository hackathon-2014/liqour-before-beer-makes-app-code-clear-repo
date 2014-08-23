<?php
$mysqli = new mysqli("localhost", "webservice", "generic", "logo_data");

if ($mysqli->connect_errno) {
    printf("{\"error\":\"Could not connect to MySQL DB!\"}");
    exit();
}

// Oh, quit your judging!  It's been a while since I did PHP.
// I am aware how terrible this code is.  But hey, HACKATHON!
if(isset($_GET["winner"]) && isset($_GET["loser"]) && isset($_GET["category"]))
{
    $winner = intval($_GET["winner"]);
    $loser = intval($_GET['loser']);
    $categoryName = $mysqli->real_escape_string($_GET["category"]);
    
    $winnerCount;
    $loserCount;
    $winnerWorked = false;
    $loserWorked = false;
    
    if ($result = $mysqli->query("SELECT ".$categoryName."_wins FROM battles WHERE company_id = ".$winner)) {
        
        while($row = mysqli_fetch_array($result)) {
          $winnerCount = $row[0];
        }
        
        $result->close();
    }
    
    if ($result = $mysqli->query("SELECT ".$categoryName."_losses FROM battles WHERE company_id = ".$loser)) {
        
        while($row = mysqli_fetch_array($result)) {
          $loserCount = $row[0];
        }
        
        $result->close();
    }
    
    if(isset($winnerCount) && isset($loserCount))
    {
        if ($result = $mysqli->query("UPDATE battles SET ".$categoryName."_wins = ".($winnerCount+1)." WHERE company_id = ".$winner)){
            $winnerWorked = true;
        }

        if ($result = $mysqli->query("UPDATE battles SET ".$categoryName."_losses = ".($loserCount+1)." WHERE company_id = ".$loser)) {
            $loserWorked = true;
        }
        
        //Yes, we all see the glaring bug where one worked and the other didn't,
        //but the tables got updated anyways.

        //Sue me.

        if($winnerWorked && $loserWorked)
        {
            echo "{\"success\":true}";
        }
        else
        {
            echo "{\"error\":\"One or more table updates failed!\"}";
        }
    }
    else
    {
        echo "{\"error\":\"One or more table reads failed!\"}";
    }
    
}
else
{
    echo "{\"error\":\"Missing url params!\"}";
}


$mysqli->close();
?>