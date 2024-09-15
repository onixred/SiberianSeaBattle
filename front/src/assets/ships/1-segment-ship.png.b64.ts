const dataUrl = 'data:image/png;base64,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';
export default dataUrl;