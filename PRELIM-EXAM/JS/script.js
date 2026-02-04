// 1. DATA POOLS
const firstNames = ["James", "Mary", "Robert", "Patricia", "Michael", "Jennifer", "William", "Linda", "David", "Elizabeth", "Richard", "Barbara", "Joseph", "Susan", "Thomas", "Jessica", "Christopher", "Sarah", "Charles", "Karen", "Mark", "Nancy", "Steven", "Lisa", "Paul", "Betty", "Andrew", "Margaret", "Joshua", "Sandra"];
const lastNames = ["Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzales", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin"];
const csCourses = ["BSCS", "BSIT", "BSIS", "BSCpE"];

let studentData = [];

// Generate 100 Students
for (let i = 0; i < 100; i++) {
    const fName = firstNames[Math.floor(Math.random() * firstNames.length)];
    const lName = lastNames[Math.floor(Math.random() * lastNames.length)];
    
    studentData.push({
        id: (202600000 + Math.floor(Math.random() * 99999)).toString(),
        name: `${fName} ${lName}`,
        course: csCourses[Math.floor(Math.random() * csCourses.length)],
        prelim: Math.floor(Math.random() * 41) + 60,
        midterm: Math.floor(Math.random() * 41) + 60,
        finals: Math.floor(Math.random() * 41) + 60,
        exam: Math.floor(Math.random() * 41) + 60,
        attendance: Math.floor(Math.random() * 21) + 80
    });
}

// 2. UTILITY: Numeric Restriction
function restrictInputs() {
    const ids = ["idInput", "prelimInput", "midtermInput", "finalsInput", "examInput", "attendanceInput"];
    ids.forEach(id => {
        const el = document.getElementById(id);
        if (el) {
            el.oninput = function() {
                this.value = this.value.replace(/[^0-9]/g, '');
                if (id === "idInput" && this.value.length > 9) this.value = this.value.slice(0, 9);
            };
        }
    });
}

// 3. RENDER
function render(dataToRender = studentData) {
    const tbody = document.getElementById("tableBody");
    if (!tbody) return;
    tbody.innerHTML = "";

    dataToRender.forEach((student) => {
        // Updated Calculation to match your Java logic: (P + M + F + E + A) / 5
        const totalGrade = ((student.prelim + student.midterm + student.finals + student.exam + student.attendance) / 5).toFixed(2);
        const statusClass = totalGrade >= 75 ? 'pass' : 'fail';

        const row = document.createElement("tr");
        row.style.cursor = "pointer";
        row.onclick = () => deleteStudentById(student.id);

        row.innerHTML = `
            <td>${student.id}</td>
            <td><strong>${student.name}</strong></td>
            <td>${student.course}</td>
            <td>${student.prelim}</td>
            <td>${student.midterm}</td>
            <td>${student.finals}</td>
            <td>${student.exam}</td>
            <td>${student.attendance}</td>
            <td class="${statusClass}">${totalGrade}</td>
            <td><button class="btn-del">Delete</button></td>
        `;
        tbody.appendChild(row);
    });
}

// 4. CREATE
document.getElementById("studentForm").onsubmit = function(e) {
    e.preventDefault();
    const idVal = document.getElementById("idInput").value;
    
    if (idVal.length !== 9) {
        alert("ID must be 9 digits.");
        return;
    }

    studentData.unshift({
        id: idVal,
        name: document.getElementById("nameInput").value,
        course: document.getElementById("courseInput").value,
        prelim: parseFloat(document.getElementById("prelimInput").value) || 0,
        midterm: parseFloat(document.getElementById("midtermInput").value) || 0,
        finals: parseFloat(document.getElementById("finalsInput").value) || 0,
        exam: parseFloat(document.getElementById("examInput").value) || 0,
        attendance: parseFloat(document.getElementById("attendanceInput").value) || 0
    });

    render();
    this.reset();
};

// 5. SEARCH & DELETE
function handleSearch() {
    const term = document.getElementById("searchInput").value.toLowerCase();
    const filtered = studentData.filter(s => 
        s.name.toLowerCase().includes(term) || 
        s.id.includes(term) ||
        s.course.toLowerCase().includes(term)
    );
    render(filtered);
}

function deleteStudentById(id) {
    const index = studentData.findIndex(s => s.id === id);
    if (index !== -1 && confirm(`Delete record for ${studentData[index].name}?`)) {
        studentData.splice(index, 1);
        render();
    }
}

// 6. INITIALIZE
restrictInputs();
render();