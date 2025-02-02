require('dotenv').config();
const express = require('express');
const axios = require('axios');
const cors = require('cors');

const app = express();
app.use(cors());
const PORT = process.env.PORT || 3000;

// GitHub Repository Information from .env
const GITHUB_OWNER = process.env.GITHUB_OWNER;
const GITHUB_REPO = process.env.GITHUB_REPO;
const BRANCH = process.env.GITHUB_BRANCH || 'main';
const BASE_URL = `https://raw.githubusercontent.com/${GITHUB_OWNER}/${GITHUB_REPO}/${BRANCH}`;

/**
 * Helper function to fetch JSON file from GitHub
 */
const fetchGitHubFile = async (filePath) => {
  try {
    const response = await axios.get(`${BASE_URL}/${filePath}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching ${filePath}:`, error.message);
    return null;
  }
};

/**
 * Endpoint to list all available objects (from config.json in GitHub repo)
 */
app.get('/objects', async (req, res) => {
  const config = await fetchGitHubFile('config.json');
  if (config) {
    res.json(config.objects);
  } else {
    res.status(500).json({ error: 'Failed to load config.json' });
  }
});

/**
 * Endpoint to retrieve specific object data (from individual JSON file)
 */
app.get('/objects/:name', async (req, res) => {
  const objectName = req.params.name;
  const objectData = await fetchGitHubFile(`data/${objectName}.json`);
  if (objectData) {
    res.json(objectData);
  } else {
    res.status(404).json({ error: 'Object not found' });
  }
});

/**
 * Health check endpoint
 */
app.get('/health', (req, res) => {
  res.json({ status: 'API is running' });
});

app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
