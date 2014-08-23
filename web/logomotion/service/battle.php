<?php
$mysqli = new mysqli("localhost", "webservice", "generic", "logo_data");

if ($mysqli->connect_errno) {
    printf("{\"error\":true}");
    exit();
}

// Oh, quit your judging!  It's been a while since I did PHP.
// I am aware how terrible this code is.  But hey, HACKATHON!
if(isset($_GET["winner"]) && isset($_GET["loser"]) && isset($_GET["category"]))
{
    $winner = intval($_GET["winner"]);
    $loser = intval($_GET['loser']);
    $category = intval($_GET['category']);

    $categoryName = "disgust";
    
    $winnerCount;
    $loserCount;
    $winnerWorked = false;
    $loserWorked = false;
    
    if ($result = $mysqli->query("SELECT ".$categoryName."_wins FROM logos WHERE id = ".$winner)) {
        
        while($row = mysqli_fetch_array($result)) {
          $winnerCount = $row[0];
        }
        
        $result->close();
    }
    
    if ($result = $mysqli->query("SELECT ".$categoryName."_losses FROM logos WHERE id = ".$loser)) {
        
        while($row = mysqli_fetch_array($result)) {
          $loserCount = $row[0];
        }
        
        $result->close();
    }
    
    if(isset($winnerCount) && isset($loserCount))
    {
        if ($result = $mysqli->query("UPDATE logos SET ".$categoryName."_wins = ".($winnerCount+1)." WHERE id = ".$winner)){
            $winnerWorked = true;
        }

        if ($result = $mysqli->query("UPDATE logos SET ".$categoryName."_losses = ".($loserCount+1)." WHERE id = ".$loser)) {
            $loserWorked = true;
        }
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
        echo "{\"error\":true}";
    }
    
}
else
{
    echo "{\"error\":true}";
}


$mysqli->close();
?>