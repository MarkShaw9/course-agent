<template>
    <div v-if="visible" class="popup">
        <div class="popup-content">
            <h3>Select Major</h3>
            <el-select v-model="selectedMajor" placeholder="Please select a major" style="width: 100%">
                <el-option label="Computer Science" value="Computer Science"></el-option>
                <el-option label="Data Science" value="Data Science"></el-option>
                <el-option label="Cybersecurity" value="Cybersecurity"></el-option>
                <el-option label="Professional Engineering (Software)"
                    value="Professional Engineering (Software)"></el-option>
            </el-select>
            <div class="buttons">
                <button @click="confirmSelection" class="confirm-button">Confirm</button>
                <button @click="closePopup" class="close-button">Cancel</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        visible: {
            type: Boolean,
            default: false
        }
    },
    data() {
        return {
            selectedMajor: ''
        };
    },
    methods: {
        confirmSelection() {
            if (this.selectedMajor) {
                this.$emit('confirm', this.selectedMajor);
                this.closePopup();
            } else {
                alert('Please select a major before confirming.');
            }
        },
        closePopup() {
            this.$emit('close');
        }
    }
};
</script>

<style scoped>
.popup {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
}

.popup-content {
    background: #fff;
    padding: 20px;
    border-radius: 5px;
    width: 250px; 
    max-width: 80%; 
    height: 150px; 
    max-height: 90%; 
}


.buttons {
    margin-top: 15px;
    display: flex;
    justify-content: space-between;
}

.confirm-button,
.close-button {
    padding: 10px 20px;
    border: none;
    cursor: pointer;
    border-radius: 5px;
}

.confirm-button {
    background-color: #409eff;
    color: #fff;
}

.close-button {
    background-color: #ccc;
}
</style>
