const dataUrl = 'data:image/png;base64,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';
export default dataUrl;