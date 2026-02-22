<div class="form-container">
    <h2>Update Room Settings</h2>
    <form action="UpdateRatesServlet" method="POST">
        <label>Room Category</label>
        <select name="room_type">
            <option value="Standard">Standard Garden View</option>
            <option value="Deluxe">Deluxe Ocean Wing</option>
            <option value="Luxury">Luxury Presidential Suite</option>
        </select>

        <label>New Price (per night)</label>
        <input type="number" name="new_price" required>

        <label>New Max Capacity (Total Rooms)</label>
        <input type="number" name="new_capacity" required>

        <button type="submit">Update Room Settings</button>
    </form>
</div>