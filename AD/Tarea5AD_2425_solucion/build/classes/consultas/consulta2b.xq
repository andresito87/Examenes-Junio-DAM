for $v in //vehiculo
where $v/kilometros < 75000
return concat($v/marca/text(), " ", $v/modelo/text())