
<%@include file="../fragments/includetags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Image Upload</title>
<link href="${imgvids}/static/lib/css/dropify.min.css" type="text/css"
	rel="stylesheet" media="screen,projection">
<link
	href="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote.css"
	rel="stylesheet">
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote-lite.css"
	rel="stylesheet">
<style type="text/css">
#input_file-error {
	color: red !important;
	padding-top: 5% !important;
}

#categoryId {
	height: 100px;
}
</style>

</head>
<body class="${themecolor }">
	<jsp:include page="../fragments/header.jsp" />

	<div class="container">
		<c:if test="${not empty error}">
			<div id="card-alert" class="card red">
				<div class="card-content white-text">
					<p>
						<i class="mdi-alert-error"></i> ${error}
					</p>
				</div>
				<button type="button" class="close white-text" data-dismiss="alert"
					aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
		</c:if>
		<div id="card-alert" class="card red hide">
			<div class="card-content white-text">
				<p>
					<i class="mdi-alert-error"></i> This Image Link already exist.
				</p>
			</div>
			<button type="button" class="close white-text" data-dismiss="alert"
				aria-label="Close">
				<span aria-hidden="true">×</span>
			</button>
		</div>
		<form
			action="${imgvids}/uploadNews?${_csrf.parameterName}=${_csrf.token}"
			method="post" modelAttribute="uploadNews"
			enctype="multipart/form-data" id="formupload">
			<div class="divider"></div>
			<div class="row section">
				<div class="col s12 m4 l3">
					<p>Select An Image (Max 2MB)</p>
				</div>
				<div class="col s8 m4 l7">
					<input type="file" id="input_file" name="file" class="dropify"
						data-height="150" data-max-file-size="2M" />
				</div>

			</div>
			<div class="row section">
				<div class="col s6 m2 l2">
					<p>Subject</p>
				</div>

				<div class="input-field col s8 m3 l3">
					<input id="subject" type="text" class="validate"
						 autocomplete="off" name="subject">
					<label for="subject" class="">News Subject</label>
				</div>
			</div>

			<div class="row section">
				<div class="col s6 m2 l2">
					<p>News Link Url</p>
				</div>

				<div class="input-field col s8 m3 l3">
					<input id="newsLink" type="text" class="validate"
						onchange="checkImageLink(this)" autocomplete="off" name="newsLink">
					<label for="newsLink" class="">News Link</label>
				</div>

				<!--  <div class=" input-field col s8 m4 l4">
                      <select name="linkType">
						<option value="" disabled selected> Image Link Type</option>
						<option value="1">Image</option>
						<option value="2">Video</option>
					</select>
				</div> -->

				<div class="col s12 m2 l3">
					<p>
						Category <span style="font-size: 10px">(Please use
							CTRL+click for multiselect)</span>
					</p>
				</div>
				<div class="input-field col s12 m3 l3">
					<select name="categoryId" id="categoryId"
						class="validate browser-default" required multiple
						onchange="changeSelect()">
						<option disabled="disabled" selected>Select Category</option>
						<c:forEach items="${categorylist }" var="cat">
							<option value="${cat.id }">${cat.name }</option>
						</c:forEach>
					</select>
				</div>
			</div>

			<%--  <div class="row section">
                  <div class="col s12 m2 l3">
	                <p>Category <span style="font-size: 10px">(Please use CTRL+click for multiselect)</span> </p>
	              </div>
                  <div class="input-field col s12 m3 l3">
                    <select name="categoryId" id="categoryId" class="validate browser-default" required multiple onchange="changeSelect()">
					<option disabled="disabled" selected>Select Category</option>
                    <c:forEach items="${categorylist }" var="cat">
						<option value="${cat.id }">${cat.name }</option>
                    </c:forEach>
					</select>
                        </div>
                  </div>   --%>

			<div class="row section">
				<div class="col s12 m2 l2">
					<p>Description</p>
				</div>

				<div class="input-field col s8 m8 l9">
					<textarea id="newsText" maxlength="250" autocomplete="off"
						name="newsText" placeholder="News Description"></textarea>
				</div>
			</div>
			<div class="row section">
				<div class="col s12 m8 l9  center" style="padding-right: 193px;">
					<button class="btn btn-large waves-effect waves-light red darken-4"
						type="submit">Submit</button>
				</div>
			</div>
		</form>

	</div>

	<jsp:include page="../includes/include_js.jsp" />
	<script type="text/javascript"
		src="${imgvids}/static/lib/js/dropify.min.js"></script>
	<!-- 	  <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script> -->

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.11/summernote-lite.js"></script>
	<script type="text/javascript">
	        $(document).ready(function(){
	            // Basic
	            $('.dropify').dropify();
	
	            // Translated
	            $('.dropify-fr').dropify({
	                messages: {
	                    default: 'Glissez-déposez un fichier ici ou cliquez',
	                    replace: 'Glissez-déposez un fichier ou cliquez pour remplacer',
	                    remove:  'Supprimer',
	                    error:   'Désolé, le fichier trop volumineux'
	                }
	            });
	
	            // Used events
	            var drEvent = $('.dropify-event').dropify();
	
	            drEvent.on('dropify.beforeClear', function(event, element){
	                return confirm("Do you really want to delete \"" + element.filename + "\" ?");
	            });
	
	            drEvent.on('dropify.afterClear', function(event, element){
	                alert('File deleted');
	            });
	        });
	        
	        $("#formupload").validate({
	            rules: {
	            	/* imageLink: {
	                    required: true,
	                   
	                }, */
	                file :{
	                	required: true,
	                },
	                type :{
	                	required: true,
	                },
	           },
	            //For custom messages
	            messages: {

	            },
	            errorElement : 'div',
	            errorPlacement: function(error, element) {
	              var placement = $(element).data('error');
	              if (placement) {
	                $(placement).append(error)
	              } else {
	                error.insertAfter(element);
	              }
	            }
	         });	        
	       
	        function checkImageLink(text){
	        	var textValue=text.value;
	        	$.post("${imgvids}/checkVideoLink", {urllink: textValue , from :'news'}, function(result){
	            	console.log(result)
	        		if(result){
	        			$("#card-alert").removeClass("hide");
	        			$("#imgLink").val("");
	        		}else{
	        			$("#card-alert").addClass("hide");
	        		}
	            });
	        	
	        }
	        
	        function changeSelect(){
	        	console.log("------",$("#categoryId").val());
	        }
	        $("#newsText").summernote({
	        	 height: 150,
	             callbacks: {
	               onKeydown: function(e) {
	                 var t = e.currentTarget.innerText;
	                 if (t.trim().length >= max) {
	                   //delete key
	                   if (e.keyCode != 8)
	                     e.preventDefault();
	                   // add other keys ...
	                 }
	               },
	               onKeyup: function(e) {
	                 var t = e.currentTarget.innerText;
	                 if (typeof callbackMax == 'function') {
	                   callbackMax(max - t.trim().length);
	                 }
	               },
	               onPaste: function(e) {
	                 var t = e.currentTarget.innerText;
	                 var bufferText = ((e.originalEvent || e).clipboardData || window.clipboardData).getData('Text');
	                 e.preventDefault();
	                 var all = t + bufferText;
	                 document.execCommand('insertText', false, all.trim().substring(0, 1900));
	                 if (typeof callbackMax == 'function') {
	                   callbackMax(max - t.length);
	                 }
	               }
	             }
	        });
	    </script>

</body>
</html>