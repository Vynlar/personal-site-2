/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["public/**/*.html"],
  theme: {
    extend: {
      fontFamily: {
        serif: '"Gloock"',
      },
      colors: {
        brand: {
          200: "#e6a510",
        }
      },
    },
    plugins: [],
  }
}
