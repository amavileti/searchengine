<%@ page contentType="text/html; charset=ISO-8859-1" %>
<jsp:include page="imports.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
 <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
 <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
 <script>
 $(document).ready(function(){
	$("#loadPage").click( 
		function(){
			if($("#url").val() != ""){
				url="http://localhost:8080/searchengine/admin/surl/"+encodeURIComponent($("#url").val());
				$.getJSON(url, function(data){
					$("#url").val(data.url);
					$("#description").val(data.description);
					$("#pageId").val(data.pageId);
				}
				);
			}else if($("#url").val() != ""){
				url="http://localhost:8080/searchengine/admin/sdesc/"+encodeURIComponent($("#description").val());
				$.getJSON(url, function(data){
					$("#url").val(data.url);
					$("#description").val(data.description);
					$("#pageId").val(data.pageId);
				}
			 );
			}
		}
	); 
	$("#savePage").click(
			function(){
				url="http://localhost:8080/searchengine/admin/update/";				
				$.getJSON(url, {pageId:$("#pageId").val(), 
					url:$("#url").val(),
					description:$("#description").val()},
					function(data){});
			}
	);
 }
 );
 </script>
 <script> 
 function validate()
 {
	 if(document.f1.URL.value=="")
	 {
		 alert("Please Enter the URL!!!");
		 document.f1.URL.focus();
	 	return false;
	 }
	 else if(document.f1.Description.value=="")
	{
		 alert("Please Enter the Description!!!");
		 document.f1.Description.focus();
	 	return false;
	}
	 return true;
	}</script>
  </head>
 <body bgcolor="#FFF8C6">
 <form name=f1 onSubmit="return validate()">
 <IMG SRC="./Images/heading.png"/>
 <div>
 	<p align="center" ><b>Admin Page</b></p>
 	<table align="center">
 	<tbody>
 	<tr><td>URL:</td><td><input type="text" name="URL" size="100" id="url" /></td></tr>
 	<br><br><tr><td>Description:</td><td><input type="text" name="Description"  size="100" id="description" /></td></tr>
 	<tr><td><input type="hidden" id="pageId" /></td></tr>
 	<tr><td colspan=2><br/></td></tr>
 	<tr><td><button id="loadPage">loadPage</button></td>
<td><button id="savePage">savePage</button></td></tr></table>
 	</tbody>
 	</table>
 </div>
 </body>
 </html>