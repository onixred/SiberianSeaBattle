const dataUrl = 'data:image/png;base64,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';
export default dataUrl;