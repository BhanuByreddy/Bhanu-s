let recordbtn = document.querySelector(".record-btn");


let recorder;

let isRecording=false;
let chunks=[]
navigator.mediaDevices.getUserMedia({video:true,audio:true})
.then((stream)=>{
     let element = document.querySelector(".video");
     element.srcObject=stream;                                
     recorder = new MediaRecorder(stream);
     
     recorder.addEventListener("start",(e)=>{
          chunks=[];
     })
     recorder.addEventListener("dataavailable",(e)=>{
         chunks.push(e.data);
     })
     recorder.addEventListener("stop",(e)=>{
         let blob = new Blob(chunks,{type:"video/mp4"});
         let videoURL =  URL.createObjectURL(blob);
         let a = document.createElement('a');        // Creating an anchor tag and setting download Attributes upon clicking this downloads
         a.href = videoURL;
         a.download = "stream.mp4";
         a.click();
     })
     recordbtn.addEventListener("click", (e) => {
        if (!isRecording) {
            recorder.start();
            isRecording = true;
        } else {
            recorder.stop();
            isRecording = false;
        }
    });
               
})
.catch((error)=>{
   console.log("Some Error Occured"+error);
});

let capture;
let capturebtn  = document.querySelector(".capture-btn");

navigator.mediaDevices.getUserMedia({video:true,audio:false})          // Returns a promise then -> resolve and Error -> reject
.then((stream)=>{
   let element = document.querySelector(".video_capture");
   element.srcObject = stream;
   const track = stream.getVideoTracks()[0];                     // Capture the Audio Track
   capture = new ImageCapture(track);
             
   capturebtn.addEventListener("click",async()=>{
      try {
            const blob = await capture.takePhoto();  // Might fail
            const videoURL = URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = videoURL;
            a.download = "photo.jpg";
            a.click();
        } catch (err) {
            console.error("Error capturing photo:", err); // Handle error
       }
   })
});
