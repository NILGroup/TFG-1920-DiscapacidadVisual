<?php
	$conexion = mysql_connect("mysql.hostinger.es","u328937656_base1","proyectosi1");
	if (!$conexion){
		die('No se ha podido conectar con la base de datos: ' . mysql_error());
	}

	mysql_select_db("u328937656_base1", $conexion);
	
	if(isset($_GET["mac"])){
		$mac=$_GET["mac"]; 
	}
	if(isset($_GET["x"])){
		$x=$_GET["x"]; 
	}
	if(isset($_GET["y"])){
		$y = $_GET["y"];
	}
	if(isset($_GET["z"])){
		$z = $_GET["z"];
	}
	if(isset($_GET["strength"])){
		$fuerza = $_GET["strength"];
	}
	/*if(isset($_GET["sob"])){
		$sobreescribir = $_GET["sob"];
	}*/
	if(!isset($mac) or !isset($x) or !isset($y) or !isset($z) or !isset($fuerza)){
		echo "Error en el número de parámetros";
	}else{
		$query = "SELECT MAC FROM repository WHERE MAC = '".$mac."' AND x = ".$x." AND y = ".$y." AND z = ".$z." AND strength = ".$fuerza .";";
		$result = mysql_query($query);
		if(mysql_num_rows($result)==0){
			$query="INSERT INTO repository VALUES('".$mac."',".$x.",".$y.",".$z.",".$fuerza.");";
			$result=mysql_query($query);
			if($result==true){
				echo "OK";
			}else{
				echo "Error inesperado.";
			}
			/*if(isset($sobreescribir)){
				if($sobreescribir=true){
					$query="UPDATE repository SET strength = ".$fuerza." WHERE MAC = '".$mac."' AND x = ".$x." AND y = ".$y." AND z = ".$z.";";
					$result = mysql_query($query);
					if($result==true){
						echo "OK";
					}else{
						echo "Error al intentar sobreescribir la posición.";
					}
				}
			}else{
				echo "Esa posición ya se encuentra registrada";
			}
		}else{*/
			
		}
		mysql_close($conexion);
	}

?>