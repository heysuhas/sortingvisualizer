class SortingVisualizer {
    constructor() {
        this.array = [];
        this.canvas = document.getElementById('sorting-canvas');
        this.ctx = this.canvas.getContext('2d');
        this.algorithmSelect = document.getElementById('algorithm-select');
        this.arrayInput = document.getElementById('array-input');
        this.setArrayBtn = document.getElementById('set-array');
        this.startSortingBtn = document.getElementById('start-sorting');
        this.speedSlider = document.getElementById('speed-slider');

        this.setArrayBtn.addEventListener('click', () => this.setArray());
        this.startSortingBtn.addEventListener('click', () => this.startSorting());

        this.resizeCanvas();
        window.addEventListener('resize', () => this.resizeCanvas());

        this.docButton = document.getElementById('doc-button');
        this.closeDocButton = document.getElementById('close-doc');
        this.documentation = document.getElementById('documentation');

        this.docButton.addEventListener('click', () => this.showDocumentation());
        this.closeDocButton.addEventListener('click', () => this.hideDocumentation());
    }

    resizeCanvas() {
        this.canvas.width = this.canvas.offsetWidth;
        this.canvas.height = this.canvas.offsetHeight;
        this.drawArray();
    }

    setArray() {
        const input = this.arrayInput.value.split(',').map(num => parseInt(num.trim()));
        if (input.some(isNaN)) {
            alert('Please enter valid numbers separated by commas.');
            return;
        }
        this.array = input;
        this.drawArray();
    }
    showDocumentation() {
        this.documentation.style.display = 'block';
    }

    hideDocumentation() {
        this.documentation.style.display = 'none';
    }

    drawArray(highlightIndices = []) {
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        const barWidth = this.canvas.width / this.array.length;
        const scaleFactor = this.canvas.height / Math.max(...this.array);

        this.array.forEach((value, index) => {
            const x = index * barWidth;
            const y = this.canvas.height - value * scaleFactor;
            const height = value * scaleFactor;

            // Create gradient for each bar
            const gradient = this.ctx.createLinearGradient(x, y, x, this.canvas.height);
            gradient.addColorStop(0, '#00ffff');
            gradient.addColorStop(1, '#ff00ff');

            this.ctx.fillStyle = highlightIndices.includes(index) ? '#ffffff' : gradient;
            this.ctx.fillRect(x, y, barWidth - 1, height);

            // Add glow effect
            this.ctx.shadowColor = '#00ffff';
            this.ctx.shadowBlur = 10;
            this.ctx.fillRect(x, y, barWidth - 1, height);
            this.ctx.shadowBlur = 0;

            // Add value text
            this.ctx.fillStyle = '#ffffff';
            this.ctx.font = '12px Orbitron';
            this.ctx.textAlign = 'center';
            this.ctx.fillText(value.toString(), x + barWidth / 2, y - 5);
        });
    }

    async startSorting() {
        const algorithm = this.algorithmSelect.value;
        this.startSortingBtn.disabled = true;
        this.setArrayBtn.disabled = true;

        try {
            const response = await fetch(`/api/sort/${algorithm}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(this.array),
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const sortingSteps = await response.json();

            for (const step of sortingSteps) {
                this.array = step;
                this.drawArray();
                await this.sleep();
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred while sorting. Please try again.');
        } finally {
            this.startSortingBtn.disabled = false;
            this.setArrayBtn.disabled = false;
        }
    }

    sleep() {
        return new Promise(resolve => setTimeout(resolve, 101 - this.speedSlider.value));
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new SortingVisualizer();
});