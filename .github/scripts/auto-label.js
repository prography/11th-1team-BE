const managedLabels = [
  '✨ feat',
  '🐛 bug',
  '♻️ refactor',
  '🧩 api',
  '🗄️ db',
  '⚡ performance',
  '✅ test',
  '📝 docs',
  '🚚 chore',
  '🔒 security',
];

const prTypeRules = [
  { pattern: /-\s*\[[xX]]\s*Feat\b/i, label: '✨ feat' },
  { pattern: /-\s*\[[xX]]\s*Bug\b/i, label: '🐛 bug' },
  { pattern: /-\s*\[[xX]]\s*Refactor\b/i, label: '♻️ refactor' },
  { pattern: /-\s*\[[xX]]\s*Chore\b/i, label: '🚚 chore' },
  { pattern: /-\s*\[[xX]]\s*API\b/i, label: '🧩 api' },
  { pattern: /-\s*\[[xX]]\s*DB\b/i, label: '🗄️ db' },
  { pattern: /-\s*\[[xX]]\s*Performance\b/i, label: '⚡ performance' },
  { pattern: /-\s*\[[xX]]\s*Test\b/i, label: '✅ test' },
  { pattern: /-\s*\[[xX]]\s*Docs\b/i, label: '📝 docs' },
  { pattern: /-\s*\[[xX]]\s*Security\b/i, label: '🔒 security' },
];

function collectLabels(body) {
  const labels = new Set();

  for (const rule of prTypeRules) {
    if (rule.pattern.test(body)) {
      labels.add(rule.label);
    }
  }

  return labels;
}

module.exports = async ({ github, context }) => {
  const pr = context.payload.pull_request;
  if (!pr) {
    return;
  }

  const { owner, repo } = context.repo;
  const labels = collectLabels(pr.body || '');

  const currentLabels = pr.labels.map((label) => label.name);
  const labelsToAdd = [...labels].filter((label) => !currentLabels.includes(label));
  const labelsToRemove = currentLabels.filter(
    (label) => managedLabels.includes(label) && !labels.has(label),
  );

  if (labelsToAdd.length > 0) {
    await github.rest.issues.addLabels({
      owner,
      repo,
      issue_number: pr.number,
      labels: labelsToAdd,
    });
  }

  for (const label of labelsToRemove) {
    await github.rest.issues.removeLabel({
      owner,
      repo,
      issue_number: pr.number,
      name: label,
    }).catch((error) => {
      if (error.status !== 404) {
        throw error;
      }
    });
  }
};
