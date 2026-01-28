const API_URL = "http://localhost:8080/api/po";

function loadReports() {

    // 🔶 Pending POs
    fetch(`${API_URL}/pending`)
        .then(res => res.json())
        .then(data => {
            const pendingTable = document.getElementById("pendingPOs");
            pendingTable.innerHTML = "";

            data.forEach(po => {
                pendingTable.innerHTML += `
                    <tr>
                        <td>${po.id}</td>
                        <td>${po.vendor ? po.vendor.name : "N/A"}</td>
                        <td>
                            <span class="status status-created">${po.status}</span>
                        </td>
                    </tr>
                `;
            });
        })
        .catch(err => console.error("Pending PO error:", err));


    // 🔶 Approved POs
    fetch(`${API_URL}/approved`)
        .then(res => res.json())
        .then(data => {
            const approvedTable = document.getElementById("approvedPOs");
            approvedTable.innerHTML = "";

            data.forEach(po => {
                approvedTable.innerHTML += `
                    <tr>
                        <td>${po.id}</td>
                        <td>${po.vendor ? po.vendor.name : "N/A"}</td>
                        <td>
                            <span class="status status-approved">${po.status}</span>
                        </td>
                    </tr>
                `;
            });
        })
        .catch(err => console.error("Approved PO error:", err));
}

// 🔁 Auto refresh every 5 sec
setInterval(loadReports, 5000);
loadReports();
