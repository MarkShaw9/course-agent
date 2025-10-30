<template>
    <div>
        <h2>User Login Records</h2>
        <input type="text" v-model="username" placeholder="Enter username to search" />
        <button @click="fetchLoginRecords">Search Login Records</button>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Login Time</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="record in loginRecords" :key="record.id">
                    <td>{{ record.id }}</td>
                    <td>{{ record.username }}</td>
                    <td>{{ record.loginTime }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>

import loginRecordService from '@/api/loginRecordService';

export default {
    data() {
        return {
            username: '',
            loginRecords: []
        };
    },
    methods: {
        // Get all login records
        fetchAllLoginRecords() {
            loginRecordService.getAllLoginRecords()
                .then(response => {
                    this.loginRecords = response.data;
                })
                .catch(error => {
                    console.error("Error fetching all login records:", error);
                });
        },
        // Get login records by username
        fetchLoginRecords() {
            if (this.username) {
                loginRecordService.getLoginRecordsByUsername(this.username)
                    .then(response => {
                        this.loginRecords = response.data;
                    })
                    .catch(error => {
                        console.error("Error fetching login records by username:", error);
                    });
            } else {
                // If no username is entered, fetch all login records
                this.fetchAllLoginRecords();
            }
        }
    },
    created() {
        // Fetch all login records by default
        this.fetchAllLoginRecords();
    }
};
</script>

<style scoped>
table {
    width: 100%;
    border-collapse: collapse;
}

th,
td {
    padding: 8px;
    text-align: left;
    border-bottom: 1px solid #ddd;
}

th {
    background-color: #f4f4f4;
}</style>