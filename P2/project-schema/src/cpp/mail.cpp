
#include <dat_http/cgi.hpp>
#include <dat_db/ddb.hpp>
#include <iostream>     // cout
#include <dat_http/params.hpp>
#include <string>
#include <map>
#include <dat_http/session.hpp>

using namespace std;
using namespace dat_base;
using namespace dat_http;
using namespace dat_db;


Response mailApp(Request request);

int main(int argc, char **argv, char **env)
{
    cgiApp(env, mailApp);
    return 0;
}


// Nom del directori arrel de la base de dades (relatiu al directori on s'ubica el CGI)
string db_dir = "mail-db";

Response page(Request request);
ByteString ChatPage();
Response error_page(const string& msg);
//Response errorResp (Status st, string msg);
bool getLoginR (string user, string password);
ByteString loginPage();
// ------
/*
Response add(const Query &params);
Response mark(const Query &params);
Response remove(const Query &params);
Response mark(const Query &params);
Response remove(const Query &params);
string new_seq();


*/


Response mailApp(Request request) //request és el CGI
{
    try {

        // processat del CGI

      string pathInfo  = request.rawPathInfo(); 
      
      if(pathInfo == "/login"){
	
	if(request.method() == "GET"){
	  
	  ResponseHeaders hs;
	  hs.push_front(Header("content-Type", MIME_HTML));
	  return Response(ok200, hs, loginPage());
	}
	Query info = postParams(request);
	if (request.method()=="POST"){
	  Maybe<string> mb_button_Login = lookupParam("Login",info);
	     
	    if(!isNothing(mb_button_Login)){
	      Maybe<string> mb_usuario = lookupParam("User", info);
	      Maybe<string> mb_contra = lookupParam("Password", info);
	      string cgi_user = fromJust(mb_usuario);
	      string cgi_password = fromJust(mb_contra);
	   
	      if ( getLoginR(cgi_user, cgi_password)){
		
		pair<SessionMap, SaveSession> session = clientLoadSession(request); //session guarda la cookie si hay
		session.first.insert(pair<string,string>("User", cgi_user));  
		ResponseHeaders hs = session.second(session.first);
		hs.push_back(Header("Content-Type",MIME_HTML));
		//hs.push_front(Header("location", pathInfo + "/inbox"));
		return Response(ok200, hs, ChatPage());
		  
	      }else{
		
		//return error autenticació           ---------//error_page(msg);
		
	      }
	      }/*else if(!isNothing(mb_button_SendMsg)){
		ResponseHeaders hs;
		hs.push_front(Header("Content-Type",MIME_HTML));
		return Response(ok200,hs,ChatPage());
	      }*/
	
	}else 
	  }
    }

      if (pathInfo == "/inbox"){
	  
	  Maybe<string> mb_button_SendMsg = lookupParam("Send",info);
	  
	  if(!isNothing(mb_button_SendMsg)){  //detectem que hem enfonsat el botó enviar
	  return error_page("Unknown exception");
	    
	      list<string> aux;
	      DBConnection dbc(db_dir);
	
  
	      time_t tm = time(NULL);
	      string id ="1";
	      //string id = get_sequence(msg-seg);
	      
	      
	      //REBEM UN CGI I L'HEM DE PASSAR AL MAYBESTRING
	      Maybe<string> mb_to = lookupParam("To", info);
	      Maybe<string> mb_subject = lookupParam("Subject", info);
	      Maybe<string> mb_content = lookupParam("Content", info);
	      
	      
	      string to = fromJust(mb_to);
	      string subject = fromJust(mb_subject);
	      string content = fromJust(mb_content);
	      
	      aux.push_back(id); 
	      aux.push_back("Claudia"); // el from l'extreiem de les cookies
	      aux.push_back(to);
	      //aux.push_back(to_string((long)tm));
	      aux.push_back("12:00");
	      aux.push_back(subject);
	      aux.push_back(content);
	      dbc.insert("messages",aux);
	      ResponseHeaders hs;
	      hs.push_front(Header("Content-Type",MIME_HTML));
	      return Response(ok200, hs, ChatPage());
	      
	  //detectar el dar al Intro
	  //meterlo en la dbc
	  //mostrar la web
		//recurs no trobat, error codi 404 Not Found 

		//return errorResp (notFound404, "Not Found:" + pathInfo);
	}
      }
	return page(request);//el request afegit per nosaltres
    }catch (const LibException& e) {
        return error_page("LibException: " + e.str());
    } catch (const std::exception& e) {
        return error_page(string("Standard exception: ") + e.what());
    } catch (...) {
        return error_page("Unknown exception");
    }
}


/****************************************************************
 * Sortida del CGI
 */

/*
//afegir missatge

Response add(const Query &params)
{
  
  
  while(dbc.next()){
    
    string from_db = dbc.get("from");
    string to_db = dbc.get("to");
    string time_db = dbc.get("time");
    string content_db = dbc.get("subject content");
    
}
  
  

    
     
   
    } else {
        return errorResponse(badRequest400, "Required parameter: 'message'");
    }
}

string new_seq()
{
    DBConnection dbc(db_dir);
    dbc.select("msg-seq");
    if (! dbc.next()) {
        throw DBException("No hi ha missatges");
    }
    
    string pos = dbc.get("value");
    pos = to_string(stoi(pos) + 1);
    dbc.update("msg-seq", "value", pos);
    return pos;
}

//retocar
Response mark(const Query &params)
{
    list<string> ids = lookupParams("id", params);
    DBConnection dbc(db_dir);
    for (auto it = ids.cbegin(); it != ids.cend(); it++) {
        dbc.update("tasks", "done", "true", "id", *it);
    }
    return get();
}
Response remove(const Query &params)
{
    list<string> ids = lookupParams("id", params);
    DBConnection dbc(db_dir);
    for (auto it = ids.cbegin(); it != ids.cend(); it++) {
        dbc.delete_rows("tasks", "id", *it);
    }
    return get();
}


*/

Response page(Request request)
{
  
  ByteString html;
 
      html.append("<html><head ><meta charset=\"UTF-8\">\n");
      html.append("<p><b>Assumpte:  <span style=\"color:#2E2EFE\">\n");
      //html.append("<p>").append(to).append(subject).append(content).append("</p>");
      html.append("</span></b></p>\n");
      html.append("</body></html>\n");
  
   ResponseHeaders hs;
   hs.push_back(Header("Content-Type", MIME_HTML));
   return Response(ok200, hs, html);
   
}

ByteString ChatPage()
{
    ByteString html;
    DBConnection dbc(db_dir);

    html.append("<html><head><meta charset=\"UTF-8\">\n").
        append("<title >  Servei de missatgeria</title>\n").
        append("</head><body  bgcolor=\"#ffe6f3\">\n").
        append("<center><H1><font color=\"#cc0066\">Benvingut al servei de missatgeria </H1></center></font>\n").
        append("<table style=\"width:80%\"><tr><td align=\"left\">\n");
	
    html.append(" <H3>A qui li vols enviar el missatge?</H3>\n").
	append("<FORM METHOD=\"POST\" ACTION=\"mail.cgi/inbox\">").
	append("<P> <INPUT NAME=\"To\" SIZE=\"30\"> </P>\n");
	
    html.append(" <H3>Introdueixi l'assumpte del missatge:</H3>\n").
	append("<P> <INPUT NAME=\"Subject\" SIZE=\"30\"> </P>\n");
	
    html.append("<H3>Introdueixi el seu missatge: </H3>\n");
    html.append("<textarea rows=\"4\" cols=\"50\" NAME = \"Content\"></textarea>\n").
	append("<P><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Enviar\"></P>\n").
	append("</FORM>\n").
	append("</td> \n");
	
    html.append("<td align=\"left\">\n");
  
    html.append("<table style=\"width:100%\" >\n");
    //NOFUNCIONA html.append(" <iframe src=\"/default.asp\" width=\"100%\" height=\"100%\" scrolling=\"yes\">\n");
    
    dbc.select("messages");
	 
    while (dbc.next()) {
      
      html.append("<input type=\"checkbox\" name=\"Conversa\" value=\"conversa\">\n");
      
      html.append("<p><b>Assumpte:  <span style=\"color:#2E2EFE\">\n");
      html.append(dbc.get("subject"));
      html.append("</span></b></p>\n");
      
      html.append("<p><b>Emissor: </b><em>\n");
      html.append(dbc.get("from"));
      html.append("</em></p>");
      
      html.append("<p><b>Hora: </b><em>\n");
      html.append(dbc.get("time"));
      html.append("</em></p>\n");
      
      html.append("<p><b>Missatge: </b><br><br>\n");
      html.append(dbc.get("content"));
      html.append("</br></p><hr>\n");
    }
    
    html.append("<P><INPUT TYPE=\"submit\" NAME=\"ok\" VALUE=\"Eliminar\"></P>\n");
    html.append("</table>");
    html.append("</td>\n"). 
	append("</tr></table>\n").
	append("</body></html>\n");
	
    return html;
}

Response error_page(const string& msg)
{
    ByteString html;
    
    html.append("<HTML><HEAD><TITLE>Error</TITLE></TITLE>").
      append("</HEAD><BODY bgcolor=#ffe6f3><center><H1>T'has registrat amb errors o no estàs registrat</H1></center>").
      append("<HR><H3>Torna-ho a intentar</H3> ");
    /*html.append("<html><head><meta charset=\"UTF-8\">\n").
        append("<title>Pràctica 2: Mail</title>\n").
        append("</head><body>\n").
        append("<h2>ERROR</h2>").
        append("<h3><font color=\"red\">").append(msg).append("</font></h3>\n").
        append("<p><a href=\"#\">Pàgina de sessió</a></p>\n");*/
        html.append("</body></html>\n");

    ResponseHeaders hs;
    hs.push_back(Header("Content-Type", MIME_HTML));
    return Response(internalServerError500, hs, html);
}

//Response errorResp (Status st, string msg){

	/*ResponseHeaders hs;
	hs.push_front(Header("Content-Type", MIME_HTML));
	return Response (st, hs, error_page(msg)); //string q serà contingut HTML*/
//}


bool getLoginR (string user, string password){
  
  DBConnection dbc(db_dir);
  dbc.select("users");
     
  while(dbc.next()){
    
    string usuari_db = dbc.get("name");
    string pass_db = dbc.get("password");
    
    if((user.compare(usuari_db) == 0) && (password.compare(pass_db) == 0)){    
      return true;
    }
  }
  return false;
}

ByteString loginPage(){
  
  ByteString html;
  
  html.append("<HTML><HEAD><TITLE>Acces</TITLE></TITLE>").
      append("</HEAD><BODY bgcolor=#ffe6f3><center><H1>Accedeixi al seu espai.</H1></center>").
      append("<HR><H3>Introdueixi el nom d'usuari:</H3> ").
      append("<FORM METHOD=\"POST\" ACTION=\"mail.cgi\"><P> <INPUT NAME=\"User\" SIZE=\"30\"> </P>").
      append("<H3>Introdueixi el Password:</H3>").
      append("<P> <INPUT TYPE= \"Password\" NAME=\"Password\" SIZE=\"30\"> </P>").
      append("<P><INPUT TYPE=\"submit\" NAME=\"Login\" VALUE=\"Accedir\"></P></FORM>").
      append("<P>Claudia Ramirez, Sergi Mas</p>").
      append("<HR><HR></BODY></HTML>");
  
      return html;
}