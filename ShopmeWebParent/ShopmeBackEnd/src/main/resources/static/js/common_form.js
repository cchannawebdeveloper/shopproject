$(document).ready(function() {

		//alert("common form work alert");
		$("#btn-cancel").on("click", function() {
			window.location = moduleURL;
			
		});
		
		$("#fileImage").change(function() {
			
			if(!checkFileSize(this)) {
				return;
			}
			
			showImageThumbnail(this);
			
			
//			fileSize = this.files[0].size;
//			if(fileSize > MAX_FILE_SIZE) {
//				this.setCustomValidity("You must choose an image less than "+MAX_FILE_SIZE/1004.8+"KB!");
//				this.reportValidity();
//			} else {
//				this.setCustomValidity("");
//				showImageThumbnail(this);
//			}
			 
		});
		
	});

	
	function showImageTest() {
		alert("Yesss");
	}

	
	function showImageThumbnail(fileInput) {
		var file = fileInput.files[0];
		var reader = new FileReader();
		reader.onload = function(e) {
			$("#thumbnail").attr("src", e.target.result);
		};
		
		reader.readAsDataURL(file);
	}
	
	
	function checkFileSize(fileInput) {
		
		fileSize = fileInput.files[0].size;
		if(fileSize > MAX_FILE_SIZE) {
			fileInput.setCustomValidity("You must choose an image less than "+ MAX_FILE_SIZE +"bytes!");
			fileInput.reportValidity();
			return false;
		} else {
			fileInput.setCustomValidity("");
			//showImageThumbnail(fileInput);
			return true;
		}
		
	}
	

	function showModalDialog(title, message) {
		$("#modalTitle").text(title);
		$("#modalBody").text(message);
		$("#modalDialog").modal();
	}

	// function showModalDialog(title){
	// 	$("#modalTitle").text(title);
	// 	$("#modalDeailog").modal();
	// }
	
	function showErrorModal(message) {
		showModalDialog("Error", message);
	}
	
	function showWarningModal(message) {
		showModalDialog("Warning", message);
	}

	function alertTest() {
		alert("alert Test Function work!!");
	}