const stripe = Stripe('pk_test_51QvWXqCIAT0CWN6Psq2nVsUTAZvlu0WOHNxwP5PMtaPwUwpmQk27ecBj12ImqYmJbAFSOSCDbZ5M4GLiv3Wyfrw1005uRf7bzo');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
  stripe.redirectToCheckout({
    sessionId: sessionId
  })
});