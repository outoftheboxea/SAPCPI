# Use the official Node.js image (adjust the version as needed)
FROM node:18-alpine

# Create app directory
WORKDIR /app

# Copy package.json and package-lock.json (if available) and install dependencies
COPY package.json package-lock.json* ./
RUN npm install

# Copy the rest of your app's source code
COPY . .

# Expose the port your app runs on
EXPOSE 3000

# Start the application
CMD ["npm", "start"]
