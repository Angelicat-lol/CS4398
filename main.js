        let isAdminAuthenticated = false;

        let currentUser = {
            isLoggedIn: false,
            name: "Guest Traveler",
            email: "guest@mytrip.com",
            role: "Guest"
        };

        let activeBookingTarget = null; // Item currently open in modal

        // Automated State Management with Dynamic Bookings Tracking
        let tripState = {
            startLocation: 'Austin, TX',
            waypoint: 'New Braunfels, TX',
            destination: 'San Antonio, TX',
            budget: 200,
            selectedMPG: 28,
            vehicleType: 'Sedan',
            currentGasPriceAvg: 3.42,
            distanceMiles: 80,
            fuelCost: 9.77,
            selectedHotel: null,
            selectedActivities: [],
            savedTrips: [],
            bookings: [] // Holds confirmed reservation objects
        };

        let pendingVerifications = [
            { id: 101, name: 'Angelica Alvarado', role: 'Vendor Listing', docType: 'Business License #TX-8821' },
            { id: 102, name: 'John Doe', role: 'Traveler Account', docType: "Driver's License (State ID)" }
        ];

        let mockHotels = [
            { id: 1, name: 'Budget Inn Express', price: 65, rating: '4.2 Rating' },
            { id: 2, name: 'Riverwalk Economy Suites', price: 95, rating: '4.5 Rating' },
            { id: 3, name: 'Roadside Travel Lodge', price: 50, rating: '3.8 Rating' }
        ];

        let mockGasStations = [
            { id: 1, name: 'Bucees Travel Center ($3.25/gal)' },
            { id: 2, name: 'Shell Plaza ($3.40/gal)' }
        ];

        let mockActivities = [
            { id: 1, name: 'Historic Alamo Tour Pass', price: 12 },
            { id: 2, name: 'Botanical Garden Entry', price: 15 }
        ];

        // Background Live Market Ticker System
        function updateLiveMarketAverages() {
            const variance = (Math.random() * 0.10 - 0.05);
            tripState.currentGasPriceAvg = parseFloat((3.42 + variance).toFixed(2));
            
            document.getElementById('headerGasAvgDisplay').textContent = `$${tripState.currentGasPriceAvg.toFixed(2)}`;
            recalculateFuelCost();
        }

        function recalculateFuelCost() {
            tripState.fuelCost = parseFloat(((tripState.distanceMiles / tripState.selectedMPG) * tripState.currentGasPriceAvg).toFixed(2));
            renderAllViews();
        }

        // Vehicle Preset Selection Engine
        function selectMPGPreset(type, mpg) {
            document.querySelectorAll('.preset-btn').forEach(el => el.classList.remove('active'));
            document.getElementById(`preset-${type}`).classList.add('active');
            
            tripState.selectedMPG = mpg;
            tripState.vehicleType = type.charAt(0).toUpperCase() + type.slice(1);
            recalculateFuelCost();
        }

        // Navigation Controller
        function switchTab(viewId) {
            document.querySelectorAll('.view-page').forEach(el => el.classList.remove('active'));
            document.querySelectorAll('.nav-tab').forEach(el => el.classList.remove('active'));
            document.querySelectorAll('.desktop-nav-btn').forEach(el => el.classList.remove('active'));

            document.getElementById(viewId).classList.add('active');
            
            const tabName = viewId.replace('View', '');
            const mobileBtn = document.getElementById('tab-' + tabName);
            const desktopBtn = document.getElementById('dtab-' + tabName);

            if (mobileBtn) mobileBtn.classList.add('active');
            if (desktopBtn) desktopBtn.classList.add('active');

            hideAlert();

            if (viewId === 'adminView') {
                updateAdminViewUI();
            } else {
                renderAllViews();
            }
        }


        function showAlert(msg, type = 'error') {
            const box = document.getElementById('globalAlert');
            box.textContent = msg;
            box.className = `app-alert alert-${type}`;
            box.style.display = 'block';
        }

        function hideAlert() {
            document.getElementById('globalAlert').style.display = 'none';
        }

        // Booking Modal System
        function openBookingModal(id, category) {
            let item = null;
            if (category === 'Hotel') {
                item = mockHotels.find(h => h.id === id);
            } else {
                item = mockActivities.find(a => a.id === id);
            }

            if (!item) return;

            activeBookingTarget = { ...item, category };

            document.getElementById('modalItemName').textContent = item.name;
            document.getElementById('modalItemPrice').textContent = category === 'Hotel' ? `$${item.price.toFixed(2)} / night` : `$${item.price.toFixed(2)} / pass`;
            document.getElementById('bookGuestName').value = currentUser.isLoggedIn ? currentUser.name : '';

            const dynamicFields = document.getElementById('modalDynamicFields');
            const today = new Date().toISOString().split('T')[0];

            if (category === 'Hotel') {
                dynamicFields.innerHTML = `
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-field">
                            <label>Check-In Date</label>
                            <input type="date" id="bookCheckIn" class="input-box" value="${today}" required onchange="calculateModalTotal()">
                        </div>
                        <div class="form-field">
                            <label>Nights</label>
                            <input type="number" id="bookNights" class="input-box" value="1" min="1" max="14" required onchange="calculateModalTotal()">
                        </div>
                    </div>`;
            } else {
                dynamicFields.innerHTML = `
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                        <div class="form-field">
                            <label>Visit Date</label>
                            <input type="date" id="bookVisitDate" class="input-box" value="${today}" required>
                        </div>
                        <div class="form-field">
                            <label>Passes / Tickets</label>
                            <input type="number" id="bookTickets" class="input-box" value="1" min="1" max="10" required onchange="calculateModalTotal()">
                        </div>
                    </div>`;
            }

            calculateModalTotal();
            document.getElementById('bookingModal').classList.add('open');
        }

        function closeBookingModal() {
            document.getElementById('bookingModal').classList.remove('open');
            activeBookingTarget = null;
        }

        function calculateModalTotal() {
            if (!activeBookingTarget) return;

            let total = activeBookingTarget.price;
            if (activeBookingTarget.category === 'Hotel') {
                const nights = parseInt(document.getElementById('bookNights')?.value) || 1;
                total = activeBookingTarget.price * nights;
            } else {
                const tickets = parseInt(document.getElementById('bookTickets')?.value) || 1;
                total = activeBookingTarget.price * tickets;
            }

            document.getElementById('modalTotalPay').textContent = `$${total.toFixed(2)}`;
        }

        function handleConfirmBooking(e) {
            e.preventDefault();
            if (!activeBookingTarget) return;

            const guestName = document.getElementById('bookGuestName').value.trim();
            const refCode = 'RES-' + Math.floor(1000 + Math.random() * 9000);
            let detailsStr = '';
            let totalPaid = 0;

            if (activeBookingTarget.category === 'Hotel') {
                const checkIn = document.getElementById('bookCheckIn').value;
                const nights = parseInt(document.getElementById('bookNights').value) || 1;
                totalPaid = activeBookingTarget.price * nights;
                detailsStr = `${nights} Night(s) starting ${checkIn}`;

                // Auto-select booked hotel on planning board
                selectHotel(activeBookingTarget.id);
            } else {
                const visitDate = document.getElementById('bookVisitDate').value;
                const tickets = parseInt(document.getElementById('bookTickets').value) || 1;
                totalPaid = activeBookingTarget.price * tickets;
                detailsStr = `${tickets} Ticket(s) for ${visitDate}`;

                // Auto-add activity to planning board if missing
                if (!tripState.selectedActivities.some(a => a.id === activeBookingTarget.id)) {
                    toggleActivity(activeBookingTarget.id);
                }
            }

            const reservation = {
                id: refCode,
                title: activeBookingTarget.name,
                category: activeBookingTarget.category,
                guest: guestName,
                details: detailsStr,
                total: totalPaid,
                dateBooked: new Date().toLocaleDateString()
            };

            tripState.bookings.push(reservation);
            closeBookingModal();
            renderAllViews();
            showAlert(`Reservation Confirmed! Reference Code: ${refCode}`, 'info');
        }

        // Admin Security System
        function handleAdminLogin(e) {
            if (e) e.preventDefault();
            const u = document.getElementById('adminUser').value.trim();
            const p = document.getElementById('adminPass').value.trim();

            if (u === 'admin' && p === 'admin123') {
                isAdminAuthenticated = true;
                updateAdminViewUI();
                showAlert('Admin authentication successful.', 'info');
            } else {
                showAlert('Invalid Administrator Credentials.');
            }
        }

        function lockAdminSession() {
            isAdminAuthenticated = false;
            updateAdminViewUI();
            showAlert('Admin session locked.', 'info');
        }

        function updateAdminViewUI() {
            const gate = document.getElementById('adminAuthGate');
            const dashboard = document.getElementById('adminDashboardContent');

            if (isAdminAuthenticated) {
                gate.style.display = 'none';
                dashboard.style.display = 'block';
                renderAdminVerifications();
            } else {
                gate.style.display = 'block';
                dashboard.style.display = 'none';
            }
        }

        // User Account Logic
        function handleUserLogin(e) {
            if (e) e.preventDefault();
            const email = document.getElementById('loginEmail').value || 'traveler@example.com';
            
            currentUser = {
                isLoggedIn: true,
                name: email.split('@')[0].toUpperCase(),
                email: email,
                role: 'Traveler'
            };
            
            updateUserUI();
            showAlert('User logged in.', 'info');
        }

        function loginAsDemo(role) {
            currentUser = {
                isLoggedIn: true,
                name: role === 'Vendor' ? 'Angelica (Vendor)' : 'Jane Traveler',
                email: role.toLowerCase() + '@mytrip.com',
                role: role
            };
            updateUserUI();
            showAlert(`Logged in as Demo ${role}`, 'info');
        }

        function userLogout() {
            currentUser = { isLoggedIn: false, name: "Guest Traveler", email: "guest@mytrip.com", role: "Guest" };
            updateUserUI();
            showAlert('Logged out successfully.', 'info');
        }

        function updateUserUI() {
            const nameSpan = document.getElementById('headerUserName');
            const guestState = document.getElementById('profileGuestState');
            const loggedInState = document.getElementById('profileLoggedInState');

            if (currentUser.isLoggedIn) {
                nameSpan.textContent = currentUser.name.split(' ')[0];
                guestState.style.display = 'none';
                loggedInState.style.display = 'block';

                document.getElementById('profileDisplayName').textContent = currentUser.name;
                document.getElementById('profileDisplayEmail').textContent = currentUser.email;
            } else {
                nameSpan.textContent = "Guest";
                guestState.style.display = 'block';
                loggedInState.style.display = 'none';
            }
        }

        // Google Maps Integration with Waypoints
        function updateGoogleMapsWidget(start, way, dest) {
            const iframe = document.getElementById('gmapIframe');
            const link = document.getElementById('gmapExternalLink');
            
            const startClean = (start || '').trim();
            const wayClean = (way || '').trim();
            const destClean = (dest || '').trim();

            let embedUrl = '';
            let dirUrl = '';

            if (wayClean !== '') {
                const destinationWithWaypoint = `${encodeURIComponent(wayClean)}+to:${encodeURIComponent(destClean)}`;
                embedUrl = `https://maps.google.com/maps?saddr=${encodeURIComponent(startClean)}&daddr=${destinationWithWaypoint}&output=embed`;
                dirUrl = `https://www.google.com/maps/dir/?api=1&origin=${encodeURIComponent(startClean)}&destination=${encodeURIComponent(destClean)}&waypoints=${encodeURIComponent(wayClean)}`;
            } else {
                embedUrl = `https://maps.google.com/maps?saddr=${encodeURIComponent(startClean)}&daddr=${encodeURIComponent(destClean)}&output=embed`;
                dirUrl = `https://www.google.com/maps/dir/?api=1&origin=${encodeURIComponent(startClean)}&destination=${encodeURIComponent(destClean)}`;
            }

            iframe.src = embedUrl;
            link.href = dirUrl;
        }

        function startPlanningFromHome() {
            const start = document.getElementById('homeStart').value.trim();
            const dest = document.getElementById('homeDest').value.trim();

            if (!start || !dest) {
                showAlert('Locations required.');
                return;
            }

            tripState.startLocation = start;
            tripState.destination = dest;
            document.getElementById('planStart').value = start;
            document.getElementById('planDest').value = dest;

            switchTab('planView');
        }

        function generateRouteAndProceed() {
            const start = document.getElementById('planStart').value.trim();
            const way = document.getElementById('planWaypoint').value.trim();
            const dest = document.getElementById('planDest').value.trim();
            const budget = parseFloat(document.getElementById('planBudget').value);

            if (!start || !dest) {
                showAlert('Validation Error: Missing starting point or destination.');
                return;
            }

            if (isNaN(budget) || budget <= 0) {
                showAlert('Error: Invalid target budget.');
                return;
            }

            tripState.startLocation = start;
            tripState.waypoint = way;
            tripState.destination = dest;
            tripState.budget = budget;
            tripState.distanceMiles = way ? 95 : 80;

            recalculateFuelCost();
            updateGoogleMapsWidget(start, way, dest);
            switchTab('resultsView');
        }

        function selectHotel(id) {
            tripState.selectedHotel = mockHotels.find(h => h.id === id);
            renderAllViews();
        }

        function toggleActivity(id) {
            const index = tripState.selectedActivities.findIndex(a => a.id === id);
            if (index > -1) {
                tripState.selectedActivities.splice(index, 1);
            } else {
                const act = mockActivities.find(a => a.id === id);
                if (act) tripState.selectedActivities.push(act);
            }
            renderAllViews();
        }

        function updateGroupSplitter() {
            const passCount = parseInt(document.getElementById('passengersCount').value) || 1;
            const hotelPrice = tripState.selectedHotel ? tripState.selectedHotel.price : 0;
            const actPrice = tripState.selectedActivities.reduce((sum, item) => sum + item.price, 0);
            const total = tripState.fuelCost + hotelPrice + actPrice;
            
            const perPerson = (total / passCount).toFixed(2);
            document.getElementById('perPersonCostDisplay').textContent = `$${perPerson}`;
        }

        function exportItinerary() {
            window.print();
        }

        function renderAllViews() {
            const routeTitle = tripState.waypoint ? 
                `${tripState.startLocation} to ${tripState.waypoint} to ${tripState.destination}` : 
                `${tripState.startLocation} to ${tripState.destination}`;

            document.getElementById('routeBannerTitle').textContent = routeTitle;
            document.getElementById('fuelCostDisplay').textContent = `$${tripState.fuelCost.toFixed(2)} Fuel`;
            document.getElementById('routeMetrics').textContent = `Distance: ${tripState.distanceMiles} miles | ~1 hr 30 min`;

            // Hotels Render
            const hotelBox = document.getElementById('hotelList');
            hotelBox.innerHTML = '';
            mockHotels.forEach(h => {
                const isSel = tripState.selectedHotel && tripState.selectedHotel.id === h.id;
                hotelBox.innerHTML += `
                    <div class="selectable-item ${isSel ? 'active-item' : ''}" style="flex-direction: column; align-items: stretch; gap: 0.75rem;">
                        <div style="display: flex; justify-content: space-between; align-items: center;">
                            <div>
                                <div style="font-weight:700; font-size:0.95rem;">${h.name}</div>
                                <div style="font-size:0.8rem; color:var(--text-sub); margin-top:0.2rem;">${h.rating} • $${h.price}/night</div>
                            </div>
                            <button class="btn-touch ${isSel ? 'btn-primary' : 'btn-outline'}" style="width:auto; padding:0.4rem 0.75rem; font-size:0.8rem;" onclick="selectHotel(${h.id})">
                                ${isSel ? 'Selected' : 'Select'}
                            </button>
                        </div>
                        <button class="btn-touch btn-accent" style="padding: 0.5rem; font-size: 0.85rem;" onclick="openBookingModal(${h.id}, 'Hotel')">Book Direct Now</button>
                    </div>`;
            });

            // Gas Stations Render
            const gasBox = document.getElementById('gasList');
            gasBox.innerHTML = '';
            mockGasStations.forEach(g => {
                gasBox.innerHTML += `
                    <div class="selectable-item">
                        <div style="font-weight:700; font-size:0.9rem;">${g.name}</div>
                        <span class="pill-badge pill-success">On Route</span>
                    </div>`;
            });

            // Activities Render
            const actBox = document.getElementById('activityList');
            actBox.innerHTML = '';
            mockActivities.forEach(a => {
                const isSel = tripState.selectedActivities.some(item => item.id === a.id);
                actBox.innerHTML += `
                    <div class="selectable-item ${isSel ? 'active-item' : ''}" style="flex-direction: column; align-items: stretch; gap: 0.75rem;">
                        <div style="display: flex; justify-content: space-between; align-items: center;">
                            <div>
                                <div style="font-weight:700; font-size:0.9rem;">${a.name}</div>
                                <div style="font-size:0.8rem; color:var(--text-sub); margin-top:0.2rem;">$${a.price}/pass</div>
                            </div>
                            <button class="btn-touch ${isSel ? 'btn-primary' : 'btn-outline'}" style="width:auto; padding:0.4rem 0.75rem; font-size:0.8rem;" onclick="toggleActivity(${a.id})">
                                ${isSel ? 'Added' : 'Add'}
                            </button>
                        </div>
                        <button class="btn-touch btn-soft" style="padding: 0.5rem; font-size: 0.85rem;" onclick="openBookingModal(${a.id}, 'Activity')">Book Attraction Pass</button>
                    </div>`;
            });

            // Cost Summary Render
            document.getElementById('summaryStartDest').textContent = routeTitle;
            document.getElementById('summaryVehicleInfo').textContent = `Vehicle: ${tripState.vehicleType} (${tripState.selectedMPG} MPG) @ $${tripState.currentGasPriceAvg.toFixed(2)}/gal US Avg`;
            document.getElementById('summaryFuelCost').textContent = `$${tripState.fuelCost.toFixed(2)}`;
            
            const hotelPrice = tripState.selectedHotel ? tripState.selectedHotel.price : 0;
            document.getElementById('summaryHotelCost').textContent = `$${hotelPrice.toFixed(2)}`;

            const actPrice = tripState.selectedActivities.reduce((sum, item) => sum + item.price, 0);
            document.getElementById('summaryActivityCost').textContent = `$${actPrice.toFixed(2)}`;

            const total = tripState.fuelCost + hotelPrice + actPrice;
            document.getElementById('summaryTotalCost').textContent = `$${total.toFixed(2)}`;
            document.getElementById('summaryTargetBudget').textContent = `$${tripState.budget.toFixed(2)}`;

            const badge = document.getElementById('budgetBadge');
            const card = document.getElementById('budgetStatusCard');

            if (total <= tripState.budget) {
                badge.className = 'pill-badge pill-success';
                badge.textContent = 'WITHIN BUDGET';
                card.style.background = '#f0fdf4';
                card.style.borderColor = '#86efac';
            } else {
                badge.className = 'pill-badge pill-danger';
                badge.textContent = `OVER (+$${(total - tripState.budget).toFixed(2)})`;
                card.style.background = '#fef2f2';
                card.style.borderColor = '#fca5a5';
            }

            updateGroupSplitter();
            renderSavedTrips();
            renderConfirmedBookings();
        }

        function renderConfirmedBookings() {
            const listContainer = document.getElementById('confirmedBookingsList');
            const accountContainer = document.getElementById('accountBookingsList');
            const countBadge = document.getElementById('bookingCountBadge');

            countBadge.textContent = `${tripState.bookings.length} Active`;

            if (!tripState.bookings.length) {
                const emptyHtml = '<p style="color: var(--text-sub); font-size: 0.9rem;">No active reservations found.</p>';
                if (listContainer) listContainer.innerHTML = emptyHtml;
                if (accountContainer) accountContainer.innerHTML = emptyHtml;
                return;
            }

            let html = '';
            tripState.bookings.forEach(b => {
                html += `
                    <div class="selectable-item" style="flex-direction: column; align-items: flex-start; gap: 0.5rem; border-left: 4px solid var(--accent);">
                        <div style="width: 100%; display: flex; justify-content: space-between; align-items: center;">
                            <strong style="font-size: 0.95rem;">${b.title}</strong>
                            <span class="pill-badge pill-success">${b.id}</span>
                        </div>
                        <div style="font-size: 0.85rem; color: var(--text-sub);">${b.details} • Guest: ${b.guest}</div>
                        <div style="font-size: 0.9rem; font-weight: 700; color: var(--primary);">Total Paid: $${b.total.toFixed(2)}</div>
                    </div>`;
            });

            if (listContainer) listContainer.innerHTML = html;
            if (accountContainer) accountContainer.innerHTML = html;
        }

        function saveCurrentTrip() {
            const plan = { 
                id: Date.now(), 
                start: tripState.startLocation, 
                dest: tripState.destination, 
                budget: tripState.budget,
                savedBy: currentUser.isLoggedIn ? currentUser.name : 'Guest'
            };
            tripState.savedTrips.push(plan);
            renderSavedTrips();
            alert(`Trip plan saved (${plan.savedBy}).`);
        }

        function renderSavedTrips() {
            const container = document.getElementById('savedTripsContainer');
            const summaryContainer = document.getElementById('accountTripsListSummary');

            if (!tripState.savedTrips.length) {
                const emptyMsg = '<p style="color: var(--text-sub); font-size: 0.9rem;">No saved trip plans yet.</p>';
                if (container) container.innerHTML = emptyMsg;
                if (summaryContainer) summaryContainer.innerHTML = emptyMsg;
                return;
            }

            let html = '';
            tripState.savedTrips.forEach(t => {
                html += `
                    <div class="selectable-item">
                        <div>
                            <div style="font-weight:700; font-size:0.95rem;">${t.start} to ${t.dest}</div>
                            <div style="font-size:0.8rem; color:var(--text-sub); margin-top: 0.2rem;">Budget: $${t.budget} • Saved by: ${t.savedBy}</div>
                        </div>
                        <button class="btn-touch btn-soft" style="width:auto; padding:0.4rem 0.75rem; font-size:0.85rem;" onclick="loadSavedTrip(${t.id})">Open</button>
                    </div>`;
            });

            if (container) container.innerHTML = html;
            if (summaryContainer) summaryContainer.innerHTML = html;
        }

        function loadSavedTrip(id) {
            const t = tripState.savedTrips.find(x => x.id === id);
            if (t) {
                document.getElementById('planStart').value = t.start;
                document.getElementById('planDest').value = t.dest;
                document.getElementById('planBudget').value = t.budget;
                generateRouteAndProceed();
            }
        }

        // Admin Verification Management
        function renderAdminVerifications() {
            const box = document.getElementById('adminVerificationList');
            const badge = document.getElementById('pendingVerifyCount');

            if (!pendingVerifications.length) {
                box.innerHTML = '<p style="color: var(--text-sub); font-size:0.9rem;">All pending verifications cleared.</p>';
                badge.textContent = '0 Pending';
                badge.className = 'pill-badge pill-success';
                return;
            }

            badge.textContent = `${pendingVerifications.length} Pending`;
            box.innerHTML = '';
            pendingVerifications.forEach(v => {
                box.innerHTML += `
                    <div class="selectable-item" style="flex-direction: column; align-items: flex-start; gap: 0.75rem;">
                        <div style="width: 100%; display: flex; justify-content: space-between; align-items: center;">
                            <strong style="font-size: 0.95rem;">${v.name}</strong>
                            <span class="pill-badge pill-warning">${v.role}</span>
                        </div>
                        <div style="font-size: 0.85rem; color: var(--text-sub);">Doc: <code>${v.docType}</code></div>
                        <div style="display: flex; gap: 0.75rem; width: 100%; margin-top: 0.5rem;">
                            <button class="btn-touch btn-primary" style="padding: 0.5rem; font-size: 0.85rem;" onclick="approveVerification(${v.id})">Approve</button>
                            <button class="btn-touch btn-danger" style="padding: 0.5rem; font-size: 0.85rem;" onclick="rejectVerification(${v.id})">Reject</button>
                        </div>
                    </div>`;
            });
        }

        function approveVerification(id) {
            pendingVerifications = pendingVerifications.filter(v => v.id !== id);
            alert('Account approved.');
            renderAdminVerifications();
        }

        function rejectVerification(id) {
            if (confirm('Reject verification file?')) {
                pendingVerifications = pendingVerifications.filter(v => v.id !== id);
                renderAdminVerifications();
            }
        }

        function verifyUserInTable(btn) {
            const tr = btn.closest('tr');
            tr.querySelectorAll('td')[2].innerHTML = '<span class="pill-badge pill-success">Verified</span>';
            btn.remove();
        }

        function removeUser(btn) {
            if (confirm('Remove user account?')) btn.closest('tr').remove();
        }

        function addVendorListing(e) {
            e.preventDefault();
            const name = document.getElementById('vendorName').value;
            const type = document.getElementById('vendorType').value;
            const price = parseFloat(document.getElementById('vendorPrice').value);

            if (type === 'Hotel') mockHotels.push({ id: Date.now(), name, price, rating: '5.0 Rating' });
            else mockActivities.push({ id: Date.now(), name, price });

            alert('Vendor listing submitted.');
            renderAllViews();
        }

     
        // Init Sequence
        updateGoogleMapsWidget('Austin, TX', 'New Braunfels, TX', 'San Antonio, TX');
        updateUserUI();
        
        // Live market background interval
        setInterval(updateLiveMarketAverages, 45000);
