
$(document).ready(function () {
    loadJobs();
})

// save
$('#saveJobBtn').on('click', function () {
    var title = $('#jobTitle').val();
    var companyName = $('#companyName').val();
    var location = $('#jobLocation').val();
    var jobType = $('#jobType').val();
    var description = $('#jobDescription').val();

    $.ajax({
        method: 'POST',
        url: 'http://localhost:8080/api/v1/job/create',
        contentType: 'application/json',
        data: JSON.stringify({
            jobTitle:title,
            company:companyName,
            location:location,
            type:jobType,
            jobDescription:description
        }),
        success: function (response) {
            alert('Job added successfully!');
            $('#addJobForm')[0].reset();
            $('#addJobModal').modal('hide');
        },
        error: function (xhr, status, error) {
            console.error('Error saving job:', error);
            alert('Failed to add job. Please try again.');
        }
    });
});

//get all
function loadJobs(){
    $.ajax({
        url:'http://localhost:8080/api/v1/job/getall',
        method:'GET',
        dataType:'json',
        success:function(jobs){
            const $tbody = $('#jobsTableBody');
            $tbody.empty();

           jobs.forEach((job , index) => {
               const row = `
                    <tr>
                        <td>${index + 1}</td>
                        <td>${job.jobTitle}</td>
                        <td>${job.company}</td>
                        <td>${job.location}</td>
                        <td>${job.type}</td>
                        <td>${job.status}</td>
                        <td>
                            <button class="btn btn-sm btn-warning edit-btn" data-id="${job.id}">Edit</button>
                            <button class="btn btn-sm btn-danger delete-btn" data-id="${job.id}">Delete</button>
                        </td>
                    </tr>
               `;
               $tbody.append(row);
           });
        },
        error: function (xhr, status, error) {
            console.error('Error load job:', error);
            alert('Failed to load data. Please try again.');
        }
    })
}