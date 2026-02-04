function calculatePrelim() {
    // 1. Get Inputs
    const att = parseFloat(document.getElementById('attendance').value);
    const l1 = parseFloat(document.getElementById('lab1').value);
    const l2 = parseFloat(document.getElementById('lab2').value);
    const l3 = parseFloat(document.getElementById('lab3').value);

    // Validation: Ensure numbers are entered
    if (isNaN(att) || isNaN(l1) || isNaN(l2) || isNaN(l3)) {
        alert("Please enter all grades to calculate.");
        return;
    }

    // 2. REQUIRED FORMULAS
    // Lab Work Average
    const labAvg = (l1 + l2 + l3) / 3;

    // Class Standing = (Attendance * 40%) + (Lab Average * 60%)
    const classStanding = (att * 0.4) + (labAvg * 0.6);

    // 3. TARGET CALCULATIONS (The "Reverse" Formula)
    // Formula: Prelim Grade = (Exam * 0.3) + (Standing * 0.7)
    // Exam = (Target - (Standing * 0.7)) / 0.3
    const reqPass = (75 - (classStanding * 0.7)) / 0.3;
    const reqExcel = (100 - (classStanding * 0.7)) / 0.3;

    // 4. DISPLAY RAW COMPUTED VALUES
    document.getElementById('results').style.display = "block";
    
    // Displaying calculations with 2 decimal places
    document.getElementById('out-lab').innerText = labAvg.toFixed(2);
    document.getElementById('out-standing').innerText = classStanding.toFixed(2);
    
    // Showing actual scores regardless of whether they are > 100 or < 0
    document.getElementById('req-pass').innerText = reqPass.toFixed(2);
    document.getElementById('req-excel').innerText = reqExcel.toFixed(2);

    // 5. REMARKS (Based on Class Standing per instructions)
    const remarkObj = document.getElementById('remarks');
    if (classStanding >= 75) {
        remarkObj.innerHTML = "<strong>Remarks:</strong> You have a passing Class Standing.";
        remarkObj.style.color = "green";
    } else {
        remarkObj.innerHTML = "<strong>Remarks:</strong> Your Class Standing is currently below passing.";
        remarkObj.style.color = "#7a0c0c";
    }
}