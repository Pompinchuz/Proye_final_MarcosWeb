document.addEventListener('DOMContentLoaded', () => {
    // Simple animation for team cards on hover (enhance with JS if needed)
    const cards = document.querySelectorAll('.gamer-card');
    cards.forEach(card => {
        card.addEventListener('mouseenter', () => {
            card.style.transform = 'translateY(-10px)';
        });
        card.addEventListener('mouseleave', () => {
            card.style.transform = 'translateY(0)';
        });
    });
});