distinct-values(
for $x in //mecanico
return concat($x/nombre/text(), " ", $x/apellidos/text())
)