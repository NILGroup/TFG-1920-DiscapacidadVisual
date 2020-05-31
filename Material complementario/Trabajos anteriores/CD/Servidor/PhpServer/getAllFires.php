<?php
	$conexion = mysql_connect("mysql.hostinger.es","u328937656_base1","proyectosi1");
	if (!$conexion){
		die('No se ha podido conectar con la base de datos: ' . mysql_error());
	}
	mysql_select_db("u328937656_base1", $conexion);
	$query = "SELECT * FROM test.fireposition";
	$result = mysql_query($query);
	echo "[";
	if(mysql_num_rows($result)>0){
		$pos = 0;
		while($row = mysql_fetch_array($result)){
			if ($pos!=0){
				echo ",";
			}
			$aux = array();
			$aux[0] = $row["fireID"];
			$aux[1] = $row["fireX"];
			$aux[2] = $row["fireY"];
			$aux[3] = $row["fireZ"];
			echo json_encode($aux);
			$pos = $pos + 1;
		}
	}
	echo "]";
	mysql_close($conexion);

?>