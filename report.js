const API_URL = "http://localhost:8080/api/po";

function loadReports() {
    // Pending POs
    fetch(`${API_URL}/pending`)
        .then(res => res.json())
        .then(data => {
            const pendingList = document.getElementById("pendingPOs");
            pendingList.innerHTML = "";
            data.forEach(po => {
                const vendorName = po.vendor ? po.vendor.name : "Unknown Vendor";
                pendingList.innerHTML += `<li>PO #${po.id} - Vendor: ${vendorName} - Status: ${po.status}</li>`;
            });
        })
        .catch(err => console.error("Error fetching pending POs:", err));

    // Approved POs
    fetch(`${API_URL}/approved`)
        .then(res => res.json())
        .then(data => {
            const approvedList = document.getElementById("approvedPOs");
            approvedList.innerHTML = "";
            data.forEach(po => {
                const vendorName = po.vendor ? po.vendor.name : "Unknown Vendor";
                approvedList.innerHTML += `<li>PO #${po.id} - Vendor: ${vendorName} - Status: ${po.status}</li>`;
            });
        })
        .catch(err => console.error("Error fetching approved POs:", err));
}

// Refresh reports every 5 seconds (real-time)
setInterval(loadReports, 5000);
loadReports(); // initial load
