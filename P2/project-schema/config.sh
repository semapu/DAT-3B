
#
# Variables de configuració del projecte.
# IMPORTANT: Cal que prèviament s'hagi definit la variable project_dir
#

#
# Ubicació de la biblioteca cgilib i utilitats de la base de dades
# (segons la configuració dels ordinadors del laboratori de DAT)
#
include_dir=/home/pract/LabWEB/WEBprofe/usr/include
lib_dir=/home/pract/LabWEB/WEBprofe/usr/lib
lib_name=cgil
dbutil_dir=/home/pract/LabWEB/WEBprofe/usr/bin

#
# Fonts del projecte
#
src_cpp_dir=src/cpp
src_www_dir=src/www

# Noms dels fitxers de codi font (sense el sufix .cpp)
src_basenames=mail

#
# Sortida del projecte
#
build_dir=$project_dir/build
cgi_name=mail.cgi

install_www_dir=$HOME/public_html/practica2

#
# Ubicació de la base de dades
#
db_root=$install_www_dir/mail-db


