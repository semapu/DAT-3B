#!/bin/sh



echo "Content-Type: text/plain"
echo

echo "<HTML><HEAD>"
echo "<TITLE>Visites del CGI</TITLE>"
echo "</HEAD>"
echo "<BODY>"
V=$(cat comptador.txt)
expr $V + 1 > comptador.txt

echo "<H1>Tenim"
cat comptador.txt
echo "visites"

echo "</H1>"
echo "</H1>"
echo "</BODY>"
echo "</HTML>"
